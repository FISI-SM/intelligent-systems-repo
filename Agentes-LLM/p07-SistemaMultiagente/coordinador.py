"""
Archivo:      coordinador.py

Propósito:    Agente coordinador de emergencias. Descubre a los especialistas
              leyendo sus Agent Cards (como buscar en las Páginas Amarillas
              de JADE), y usa un LLM para decidir a quién enviar cada parte
              del aviso. Envía las tareas por A2A y resume las respuestas.

Ejecución (con agentes_emergencia.py ya corriendo en otra terminal;
igual en Windows CMD, PowerShell, Linux y macOS, incluido Apple
Silicon M1):
       poetry run python coordinador.py
       poetry run python coordinador.py "Choque en la Av. Venezuela, un conductor herido"
       Sin Poetry: active el entorno virtual y omita "poetry run" (vea el README).

Entrada:      Opcional: el aviso de emergencia entre comillas.

Salida:       Los agentes descubiertos y sus servicios, cada tarea A2A que
              envía el coordinador (a quién y qué) y el resumen final.

Notas:
   1. DIRECTORIO cumple el papel del DF de JADE: es la lista de direcciones
      donde buscar Agent Cards. Agregue un agente nuevo (por ejemplo,
      Defensa Civil en 9204) y el coordinador lo usará sin cambiar su lógica.
   2. El coordinador tiene UNA sola herramienta: enviar_tarea. El LLM decide
      a qué agente(s) llamar leyendo las descripciones de las tarjetas.
   3. Comparación con JADE p08: DFService.register -> Agent Card;
      DFService.search -> leer tarjetas; ACLMessage -> tarea A2A;
      el if/else del Solicitante -> la decisión del LLM.
"""

import asyncio
import sys
import uuid

import anthropic
import httpx
from anthropic import beta_async_tool
from dotenv import load_dotenv

from a2a.client import A2ACardResolver, AgentCardResolutionError, create_client
from a2a.helpers import get_artifact_text
from a2a.types import AgentCard, Message, Part, Role, SendMessageRequest

load_dotenv()

MODELO = "claude-opus-5-5"
OPCIONES = {"betas": ["server-side-fallback-2026-07-01"], "fallbacks": "default"}
DIRECTORIO = ["http://127.0.0.1:9201", "http://127.0.0.1:9202", "http://127.0.0.1:9203"]

AGENTES: dict[str, AgentCard] = {}  # nombre -> Agent Card descubierta


async def descubrir_agentes() -> None:
    """Lee la Agent Card de cada dirección del directorio."""
    async with httpx.AsyncClient() as http:
        for url in DIRECTORIO:
            try:
                tarjeta = await A2ACardResolver(http, url).get_agent_card()
            except AgentCardResolutionError:
                print(f"  (sin respuesta en {url})")
                continue
            AGENTES[tarjeta.name] = tarjeta
            print(f"  - {tarjeta.name}: {tarjeta.description}")


@beta_async_tool
async def enviar_tarea(agente: str, mensaje: str) -> str:
    """Envía una tarea por A2A a un agente especialista y devuelve su plan de acción.

    Args:
        agente: Nombre exacto del agente especialista (tal como aparece en su Agent Card).
        mensaje: Descripción de la emergencia que debe atender ese agente.
    """
    tarjeta = AGENTES.get(agente)
    if tarjeta is None:
        return f"No existe el agente '{agente}'. Agentes disponibles: {', '.join(AGENTES)}."

    print(f"  -> Tarea A2A para {agente}: {mensaje}")
    cliente = await create_client(tarjeta)
    pedido = Message(role=Role.ROLE_USER, message_id=str(uuid.uuid4()), parts=[Part(text=mensaje)])
    plan = "(sin respuesta)"
    async for evento in cliente.send_message(SendMessageRequest(message=pedido)):
        if evento.HasField("artifact_update"):
            plan = get_artifact_text(evento.artifact_update.artifact)
    await cliente.close()
    print(f"  <- {agente} respondió.")
    return plan


async def main() -> None:
    aviso = sys.argv[1] if len(sys.argv) > 1 else (
        "Sale humo de un edificio en la Av. Universitaria y hay una persona herida en la vereda."
    )

    print("Buscando agentes en el directorio...")
    await descubrir_agentes()
    if not AGENTES:
        print("No se encontró ningún agente. ¿Inició agentes_emergencia.py en otra terminal?")
        return

    # El coordinador conoce a los especialistas SOLO por sus Agent Cards
    catalogo = "\n".join(f"- {t.name}: {t.description}" for t in AGENTES.values())
    instrucciones = (
        "Eres el coordinador de emergencias de Lima. Divide el aviso y envía cada parte, "
        "con la herramienta enviar_tarea, al agente adecuado. Puedes llamar a varios agentes. "
        "Al final resume en español qué hará cada uno.\n\nAgentes disponibles:\n" + catalogo
    )

    print(f"\nAviso: {aviso}\n")
    cliente = anthropic.AsyncAnthropic()
    runner = cliente.beta.messages.tool_runner(
        model=MODELO,
        max_tokens=16000,
        system=instrucciones,
        tools=[enviar_tarea],
        messages=[{"role": "user", "content": aviso}],
        **OPCIONES,
    )
    ultimo = None
    async for mensaje in runner:
        ultimo = mensaje

    if ultimo is not None and ultimo.stop_reason != "refusal":
        print("\nCoordinador:", "".join(b.text for b in ultimo.content if b.type == "text"))
    elif ultimo is not None:
        print("El modelo no respondió esta solicitud por motivos de seguridad.")


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except anthropic.AuthenticationError:
        print("Clave de API inválida o ausente. Revise el archivo .env (vea el README).")
    except anthropic.APIConnectionError:
        print("No hay conexión con la API. Revise su internet.")
