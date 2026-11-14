"""
Archivo:      servidor_biblioteca.py

Propósito:    Crear un servidor MCP (Model Context Protocol) para la biblioteca
              de la FISI. Publica dos herramientas (buscar_libro y
              prestar_libro) y un recurso (el catálogo). Cualquier aplicación
              compatible con MCP (un agente en Python, Claude Desktop, un IDE)
              puede conectarse y usarlas sin conocer este código.

Ejecución:    No se ejecuta solo: lo inicia un cliente MCP por stdio.
              Para probarlo sin LLM (desde esta carpeta, entorno activado):
                     python cliente_prueba.py
              Para usarlo con un agente LLM: vea p05-AgenteConMCP.

Entrada:      Pedidos MCP del cliente (listar herramientas, llamar una
              herramienta, leer un recurso) por la entrada estándar.

Salida:       Respuestas MCP por la salida estándar.

Notas:
   1. @mcp.tool() publica una función como herramienta. Igual que en p03, el
      docstring y los tipos se convierten en la descripción que lee el LLM.
   2. @mcp.resource(uri) publica datos de solo lectura (el catálogo).
      Herramienta = acción; recurso = información.
   3. Transporte stdio: el cliente inicia este programa como proceso hijo y
      se comunican por entrada/salida estándar. Por eso NO use print() aquí:
      ensuciaría el canal de mensajes MCP.
   4. mcp 2.x renombró FastMCP como MCPServer. Los tutoriales con
      "from mcp.server.fastmcp import FastMCP" son de la versión 1.
   5. Los préstamos se guardan en memoria: se pierden al cerrar el servidor.
"""

from mcp.server.mcpserver import MCPServer

mcp = MCPServer("Biblioteca FISI")

# Datos de ejemplo (en un caso real: la base de datos de la biblioteca)
LIBROS = {
    "introduction to algorithms": {"autor": "Cormen et al.", "copias": 2},
    "inteligencia artificial: un enfoque moderno": {"autor": "Russell y Norvig", "copias": 1},
    "sistemas multiagente": {"autor": "Wooldridge", "copias": 0},
}
PRESTAMOS: list[tuple[str, str]] = []  # (codigo_alumno, titulo)


@mcp.tool()
def buscar_libro(titulo: str) -> str:
    """Busca un libro por su título y dice el autor y cuántas copias hay disponibles.

    Args:
        titulo: Título del libro (no importan mayúsculas ni minúsculas).
    """
    libro = LIBROS.get(titulo.lower())
    if libro is None:
        return f"No tenemos '{titulo}'. Revise el catálogo."
    return f"'{titulo}' de {libro['autor']}: {libro['copias']} copias disponibles."


@mcp.tool()
def prestar_libro(titulo: str, codigo_alumno: str) -> str:
    """Registra el préstamo de un libro a un alumno, si hay copias disponibles.

    Args:
        titulo: Título exacto del libro.
        codigo_alumno: Código de matrícula del alumno, por ejemplo 20200123.
    """
    libro = LIBROS.get(titulo.lower())
    if libro is None:
        return f"No tenemos '{titulo}'."
    if libro["copias"] == 0:
        return f"No hay copias disponibles de '{titulo}'."
    libro["copias"] -= 1
    PRESTAMOS.append((codigo_alumno, titulo))
    return f"Préstamo registrado: '{titulo}' para el alumno {codigo_alumno}. Quedan {libro['copias']} copias."


@mcp.resource("biblioteca://catalogo")
def catalogo() -> str:
    """Catálogo completo de la biblioteca (título, autor y copias)."""
    filas = [f"- {t} | {d['autor']} | {d['copias']} copias" for t, d in LIBROS.items()]
    return "Catálogo de la Biblioteca FISI:\n" + "\n".join(filas)


if __name__ == "__main__":
    mcp.run()  # transporte stdio por defecto
