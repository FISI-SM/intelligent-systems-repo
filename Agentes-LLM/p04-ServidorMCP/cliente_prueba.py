"""
Archivo:      cliente_prueba.py

Propósito:    Probar el servidor MCP de la biblioteca SIN usar un LLM: se
              conecta, descubre las herramientas y el recurso, y los usa
              directamente. Muestra que MCP es un protocolo estándar entre
              programas; el LLM es solo un cliente más.

Ejecución (con Poetry, desde la carpeta p04-ServidorMCP;
igual en Windows CMD, PowerShell, Linux y macOS, incluido Apple Silicon M1):
       poetry run python cliente_prueba.py
       Sin Poetry: active el entorno virtual y omita "poetry run" (vea el README).

Entrada:      Ninguna.

Salida:       El nombre del servidor, la lista de herramientas con su
              descripción y parámetros, el catálogo y el resultado de buscar
              y prestar un libro.

Notas:
   1. El cliente inicia el servidor como proceso hijo (transporte stdio)
      usando el mismo intérprete de Python (sys.executable).
   2. Pasos del protocolo: initialize -> list_tools / list_resources ->
      call_tool / read_resource.
   3. Este es el mismo descubrimiento que hace un agente en p05: primero
      pregunta "¿qué sabes hacer?" y luego usa lo que encontró.
"""

import asyncio
import sys
from pathlib import Path

from mcp import ClientSession
from mcp.client.stdio import StdioServerParameters, stdio_client

SERVIDOR = Path(__file__).with_name("servidor_biblioteca.py")


async def main() -> None:
    parametros = StdioServerParameters(command=sys.executable, args=[str(SERVIDOR)])

    async with stdio_client(parametros) as (lectura, escritura):
        async with ClientSession(lectura, escritura) as sesion:
            info = await sesion.initialize()
            print(f"Conectado al servidor MCP: {info.server_info.name}\n")

            # 1. Descubrimiento: ¿qué herramientas ofrece el servidor?
            herramientas = await sesion.list_tools()
            print("Herramientas:")
            for h in herramientas.tools:
                parametros_h = ", ".join(h.input_schema.get("properties", {}))
                print(f"  - {h.name}({parametros_h}): {h.description.splitlines()[0]}")

            # 2. Leer un recurso
            recursos = await sesion.list_resources()
            print("\nRecursos:", [str(r.uri) for r in recursos.resources])
            contenido = await sesion.read_resource("biblioteca://catalogo")
            print(contenido.contents[0].text)

            # 3. Llamar herramientas directamente (sin LLM)
            for nombre, argumentos in [
                ("buscar_libro", {"titulo": "Introduction to Algorithms"}),
                ("prestar_libro", {"titulo": "Introduction to Algorithms", "codigo_alumno": "20200123"}),
                ("prestar_libro", {"titulo": "Sistemas Multiagente", "codigo_alumno": "20200123"}),
            ]:
                resultado = await sesion.call_tool(nombre, argumentos)
                print(f"\n{nombre}({argumentos})\n  -> {resultado.content[0].text}")


if __name__ == "__main__":
    asyncio.run(main())
