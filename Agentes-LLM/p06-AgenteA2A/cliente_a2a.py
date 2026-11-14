"""
Archivo:      cliente_a2a.py

Propósito:    Actuar como cliente A2A: descubrir al "Tutor de Algoritmos"
              leyendo su Agent Card, enviarle una tarea y seguir sus cambios
              de estado hasta recibir el resultado.

Ejecución (con agente_a2a.py ya corriendo en otra terminal; entorno activado;
igual en Windows CMD, PowerShell, Linux y macOS, incluido Apple Silicon M1):
       python cliente_a2a.py
       python cliente_a2a.py "¿Qué es la programación dinámica?"

Entrada:      Opcional: la pregunta entre comillas.

Salida:       La Agent Card descubierta (nombre, habilidades), cada estado de
              la tarea y el artefacto con la explicación.

Notas:
   1. Descubrimiento: el cliente solo conoce la URL base; la Agent Card está
      siempre en /.well-known/agent-card.json.
   2. El cliente no sabe qué LLM ni qué código usa el agente remoto: solo
      conoce su tarjeta. A2A conecta agentes de distintos fabricantes.
   3. Si aparece "connection refused", inicie primero agente_a2a.py.
"""

import asyncio
import sys
import uuid

import httpx
from a2a.client import A2ACardResolver, AgentCardResolutionError, create_client
from a2a.helpers import get_artifact_text, get_message_text
from a2a.types import Message, Part, Role, SendMessageRequest, TaskState

URL_AGENTE = "http://127.0.0.1:9001"


async def main() -> None:
    pregunta = sys.argv[1] if len(sys.argv) > 1 else "¿Cómo funciona la búsqueda binaria y cuál es su complejidad?"

    # 1. Descubrir al agente: leer su Agent Card
    async with httpx.AsyncClient() as http:
        tarjeta = await A2ACardResolver(http, URL_AGENTE).get_agent_card()
    print(f"Agente descubierto: {tarjeta.name} - {tarjeta.description}")
    for habilidad in tarjeta.skills:
        print(f"  Habilidad: {habilidad.name} | ejemplos: {list(habilidad.examples)}")

    # 2. Enviar una tarea (un mensaje del usuario)
    cliente = await create_client(tarjeta)
    mensaje = Message(role=Role.ROLE_USER, message_id=str(uuid.uuid4()), parts=[Part(text=pregunta)])
    print(f"\nTarea enviada: {pregunta}\n")

    # 3. Seguir los eventos de la tarea hasta que termine
    async for evento in cliente.send_message(SendMessageRequest(message=mensaje)):
        if evento.HasField("task"):
            print(f"[estado] {TaskState.Name(evento.task.status.state)}")
        elif evento.HasField("status_update"):
            estado = evento.status_update.status
            nota = f" - {get_message_text(estado.message)}" if estado.HasField("message") else ""
            print(f"[estado] {TaskState.Name(estado.state)}{nota}")
        elif evento.HasField("artifact_update"):
            artefacto = evento.artifact_update.artifact
            print(f"\n[artefacto '{artefacto.name}']\n{get_artifact_text(artefacto)}\n")

    await cliente.close()


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except (AgentCardResolutionError, httpx.ConnectError):
        print(f"No se pudo conectar con {URL_AGENTE}. ¿Inició agente_a2a.py en otra terminal?")
