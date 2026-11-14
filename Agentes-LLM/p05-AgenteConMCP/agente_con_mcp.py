"""
Archivo:      agente_con_mcp.py

Propósito:    Conectar un agente LLM (Claude) al servidor MCP de la biblioteca
              (p04). El agente no tiene herramientas propias: las descubre en
              el servidor MCP al iniciar y las usa para atender al alumno.

Ejecución (con Poetry, desde la carpeta p05-AgenteConMCP;
igual en Windows CMD, PowerShell, Linux y macOS, incluido Apple Silicon M1):
       poetry run python agente_con_mcp.py
       poetry run python agente_con_mcp.py "Soy 20231045. ¿Puedo llevarme Sistemas Multiagente?"
       Sin Poetry: active el entorno virtual y omita "poetry run" (vea el README).

Entrada:      Opcional: el pedido del alumno entre comillas.

Salida:       Las herramientas descubiertas en el servidor MCP, las que usa el
              agente en cada paso y su respuesta final.

Notas:
   1. Necesita el archivo ../p04-ServidorMCP/servidor_biblioteca.py.
   2. async_mcp_tool convierte cada herramienta MCP en una herramienta de
      Claude; al llamarla, el SDK la ejecuta en el servidor MCP.
   3. Si agrega una herramienta en p04, este agente la usa sin cambiar una
      sola línea aquí: esa es la ventaja de MCP.
   4. Se usa asyncio porque la sesión MCP es asíncrona (async/await).
"""

import asyncio
import sys
from pathlib import Path

import anthropic
from anthropic.lib.tools.mcp import async_mcp_tool
from dotenv import load_dotenv
from mcp import ClientSession
from mcp.client.stdio import StdioServerParameters, stdio_client

load_dotenv()

MODELO = "claude-opus-5-5"
OPCIONES = {"betas": ["server-side-fallback-2026-07-01"], "fallbacks": "default"}
SERVIDOR = Path(__file__).resolve().parent.parent / "p04-ServidorMCP" / "servidor_biblioteca.py"


async def main() -> None:
    pedido = sys.argv[1] if len(sys.argv) > 1 else (
        "Soy el alumno 20200123. Quiero llevarme 'Introduction to Algorithms'. ¿Se puede?"
    )
    print(f"Alumno: {pedido}\n")

    parametros = StdioServerParameters(command=sys.executable, args=[str(SERVIDOR)])
    async with stdio_client(parametros) as (lectura, escritura):
        async with ClientSession(lectura, escritura) as sesion:
            await sesion.initialize()

            # 1. Descubrir las herramientas del servidor MCP
            lista = await sesion.list_tools()
            print("Herramientas descubiertas por MCP:", [h.name for h in lista.tools], "\n")

            # 2. Darle al agente esas herramientas (convertidas al formato de Claude)
            cliente = anthropic.AsyncAnthropic()
            runner = cliente.beta.messages.tool_runner(
                model=MODELO,
                max_tokens=16000,
                system="Eres el asistente de la Biblioteca FISI. Usa las herramientas para responder. Responde en español.",
                tools=[async_mcp_tool(h, sesion) for h in lista.tools],
                messages=[{"role": "user", "content": pedido}],
                **OPCIONES,
            )

            # 3. El Tool Runner hace el bucle; mostramos cada herramienta usada
            ultimo = None
            paso = 0
            async for mensaje in runner:
                paso += 1
                for bloque in mensaje.content:
                    if bloque.type == "tool_use":
                        print(f"Paso {paso}: el agente usa {bloque.name}({bloque.input}) en el servidor MCP")
                ultimo = mensaje

            if ultimo is not None and ultimo.stop_reason != "refusal":
                print("\nAgente:", "".join(b.text for b in ultimo.content if b.type == "text"))
            elif ultimo is not None:
                print("El modelo no respondió esta solicitud por motivos de seguridad.")


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except anthropic.AuthenticationError:
        print("Clave de API inválida o ausente. Revise el archivo .env (vea el README).")
    except anthropic.APIConnectionError:
        print("No hay conexión con la API. Revise su internet.")
