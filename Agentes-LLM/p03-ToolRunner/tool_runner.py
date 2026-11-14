"""
Archivo:      tool_runner.py

Propósito:    Construir el mismo agente vendedor de p02, pero dejando que el
              SDK de Claude haga el bucle (Tool Runner). Las herramientas son
              funciones normales de Python marcadas con @beta_tool: el SDK
              genera su descripción JSON a partir del nombre, los tipos y el
              docstring.

Ejecución (con Poetry; igual en Windows CMD, PowerShell,
Linux y macOS, incluido Apple Silicon M1):
       poetry run python tool_runner.py
       poetry run python tool_runner.py "¿Tienen monitores? Necesito 2."
       Sin Poetry: active el entorno virtual y omita "poetry run" (vea el README).

Entrada:      Opcional: un pedido del cliente entre comillas.

Salida:       Las herramientas que usa el agente en cada paso y la respuesta
              final.

Notas:
   1. El docstring de cada función ES la descripción que lee el LLM: escriba
      con claridad para qué sirve la herramienta y qué es cada parámetro.
   2. Las anotaciones de tipo (str, int) definen el JSON Schema de entrada.
   3. El Tool Runner ejecuta las herramientas, devuelve los resultados y se
      detiene cuando el modelo ya no pide herramientas.
   4. Compare la longitud de este archivo con p02: la lógica es la misma.
"""

import sys

import anthropic
from anthropic import beta_tool
from dotenv import load_dotenv

load_dotenv()

MODELO = "claude-opus-5-5"
OPCIONES = {"betas": ["server-side-fallback-2026-07-01"], "fallbacks": "default"}

INVENTARIO = {
    "laptop": {"stock": 5, "precio": 3200.00},
    "mouse": {"stock": 40, "precio": 35.50},
    "monitor": {"stock": 0, "precio": 890.00},
}


@beta_tool
def consultar_stock(producto: str) -> str:
    """Devuelve cuántas unidades hay de un producto de la tienda.

    Args:
        producto: Nombre del producto, por ejemplo laptop, mouse o monitor.
    """
    item = INVENTARIO.get(producto.lower())
    if item is None:
        return f"El producto '{producto}' no existe en la tienda."
    return f"Hay {item['stock']} unidades de '{producto}'."


@beta_tool
def calcular_precio(producto: str, cantidad: int) -> str:
    """Calcula el precio total en soles de comprar una cantidad de un producto.

    Args:
        producto: Nombre del producto.
        cantidad: Número de unidades a comprar.
    """
    item = INVENTARIO.get(producto.lower())
    if item is None:
        return f"El producto '{producto}' no existe en la tienda."
    return f"{cantidad} x {producto} = S/ {item['precio'] * cantidad:.2f}"


def main() -> None:
    pedido = sys.argv[1] if len(sys.argv) > 1 else "Quiero 3 laptops. ¿Hay stock y cuánto pagaría?"
    print(f"Cliente: {pedido}\n")

    cliente = anthropic.Anthropic()

    # El Tool Runner hace el bucle de p02 por nosotros
    runner = cliente.beta.messages.tool_runner(
        model=MODELO,
        max_tokens=16000,
        system="Eres el vendedor de una tienda de tecnología en Lima. Usa las herramientas antes de responder. Responde en español.",
        tools=[consultar_stock, calcular_precio],
        messages=[{"role": "user", "content": pedido}],
        **OPCIONES,
    )

    # Cada vuelta del for es una respuesta del modelo (un paso del bucle)
    ultimo = None
    for paso, mensaje in enumerate(runner, start=1):
        for bloque in mensaje.content:
            if bloque.type == "tool_use":
                print(f"Paso {paso}: el agente usa {bloque.name}({bloque.input})")
        ultimo = mensaje

    if ultimo is None:
        return
    if ultimo.stop_reason == "refusal":
        print("El modelo no respondió esta solicitud por motivos de seguridad.")
        return
    print("\nAgente:", "".join(b.text for b in ultimo.content if b.type == "text"))


if __name__ == "__main__":
    try:
        main()
    except anthropic.AuthenticationError:
        print("Clave de API inválida o ausente. Revise el archivo .env (vea el README).")
    except anthropic.APIConnectionError:
        print("No hay conexión con la API. Revise su internet.")
