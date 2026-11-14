"""
Archivo:      primera_llamada.py

Propósito:    Hacer una sola llamada a un LLM (Claude) y mostrar su respuesta.
              Sirve para ver la diferencia entre un LLM y un agente: aquí el
              modelo solo responde texto; no usa herramientas ni actúa.

Ejecución (con Poetry; igual en Windows CMD, PowerShell,
Linux y macOS, incluido Apple Silicon M1):
       poetry run python primera_llamada.py
       poetry run python primera_llamada.py "¿Qué es un agente de software?"
       Sin Poetry: active el entorno virtual y omita "poetry run" (vea el README).

Entrada:      Opcional: una pregunta entre comillas. Sin argumento usa una
              pregunta de ejemplo.

Salida:       La respuesta de Claude y los tokens usados (entrada y salida).

Notas:
   1. La clave de API se lee del archivo .env de la carpeta Agentes-LLM
      (vea el README). Nunca escriba la clave dentro del código.
   2. Un LLM solo predice texto. Para que actúe en el mundo necesita
      herramientas y un bucle: eso es un agente (vea p02).
   3. "fallbacks": si Claude rechaza la solicitud por seguridad, la API la
      reintenta con otro modelo adecuado. Se activa con el beta indicado.
   4. Pregunte dos veces lo mismo: un LLM no recuerda la llamada anterior.
      La "memoria" de una conversación la envía el programa en "messages".
"""

import sys

import anthropic
from dotenv import load_dotenv

load_dotenv()  # carga ANTHROPIC_API_KEY desde el archivo .env

MODELO = "claude-opus-5-5"
OPCIONES = {"betas": ["server-side-fallback-2026-07-01"], "fallbacks": "default"}


def main() -> None:
    pregunta = sys.argv[1] if len(sys.argv) > 1 else "Explica en dos líneas qué es un agente de software."

    cliente = anthropic.Anthropic()  # usa la clave cargada desde .env

    # Una sola llamada: enviamos un mensaje y recibimos una respuesta.
    respuesta = cliente.beta.messages.create(
        model=MODELO,
        max_tokens=16000,
        system="Eres un profesor de la FISI-UNMSM. Responde en español, claro y breve.",
        messages=[{"role": "user", "content": pregunta}],
        **OPCIONES,
    )

    if respuesta.stop_reason == "refusal":
        print("El modelo no respondió esta solicitud por motivos de seguridad.")
        return

    # La respuesta es una lista de bloques; mostramos solo los de texto.
    for bloque in respuesta.content:
        if bloque.type == "text":
            print(bloque.text)

    print(f"\n[tokens] entrada={respuesta.usage.input_tokens} salida={respuesta.usage.output_tokens}")


if __name__ == "__main__":
    try:
        main()
    except anthropic.AuthenticationError:
        print("Clave de API inválida o ausente. Revise el archivo .env (vea el README).")
    except anthropic.APIConnectionError:
        print("No hay conexión con la API. Revise su internet.")
