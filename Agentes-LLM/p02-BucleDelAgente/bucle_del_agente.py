"""
Archivo:      bucle_del_agente.py

Propósito:    Mostrar el bucle de un agente escrito a mano: el LLM piensa,
              pide usar una herramienta, el programa la ejecuta, le devuelve
              el resultado y repite hasta que el LLM da la respuesta final.
              El agente atiende una tienda con dos herramientas: consultar el
              stock y calcular el precio de una compra.

Ejecución (con el entorno virtual activado; igual en Windows CMD, PowerShell,
Linux y macOS, incluido Apple Silicon M1):
       python bucle_del_agente.py
       python bucle_del_agente.py "¿Me alcanza el stock para 4 mouse?"

Entrada:      Opcional: un pedido del cliente entre comillas.

Salida:       Cada paso del bucle ("Paso 1: usa consultar_stock(...)", el
              resultado de la herramienta) y la respuesta final del agente.

Notas:
   1. El LLM nunca ejecuta código: solo DEVUELVE un pedido (bloque tool_use)
      con el nombre de la herramienta y sus argumentos. Quien la ejecuta es
      este programa.
   2. stop_reason indica por qué se detuvo el modelo: "tool_use" (quiere una
      herramienta), "end_turn" (terminó), "refusal" (no responde).
   3. Cada tool_result lleva el mismo id que su tool_use; así el modelo sabe
      qué resultado corresponde a qué pedido.
   4. MAX_PASOS evita un bucle infinito (buena práctica en todo agente).
   5. Compare con p03: el SDK hace este mismo bucle por usted.
"""

import json
import sys

import anthropic
from dotenv import load_dotenv

load_dotenv()

MODELO = "claude-opus-5-5"
OPCIONES = {"betas": ["server-side-fallback-2026-07-01"], "fallbacks": "default"}
MAX_PASOS = 10

# ---------------------------------------------------------------------------
# 1. El "mundo" del agente: datos de la tienda (en un caso real, una base de datos)
# ---------------------------------------------------------------------------
INVENTARIO = {
    "laptop": {"stock": 5, "precio": 3200.00},
    "mouse": {"stock": 40, "precio": 35.50},
    "monitor": {"stock": 0, "precio": 890.00},
}


def consultar_stock(producto: str) -> str:
    item = INVENTARIO.get(producto.lower())
    if item is None:
        return f"El producto '{producto}' no existe en la tienda."
    return f"Hay {item['stock']} unidades de '{producto}'."


def calcular_precio(producto: str, cantidad: int) -> str:
    item = INVENTARIO.get(producto.lower())
    if item is None:
        return f"El producto '{producto}' no existe en la tienda."
    total = item["precio"] * cantidad
    return f"{cantidad} x {producto} = S/ {total:.2f}"


FUNCIONES = {"consultar_stock": consultar_stock, "calcular_precio": calcular_precio}

# ---------------------------------------------------------------------------
# 2. La descripción de las herramientas que ve el LLM (nombre, para qué sirve
#    y qué parámetros recibe, en formato JSON Schema)
# ---------------------------------------------------------------------------
HERRAMIENTAS = [
    {
        "name": "consultar_stock",
        "description": "Devuelve cuántas unidades hay de un producto de la tienda.",
        "input_schema": {
            "type": "object",
            "properties": {"producto": {"type": "string", "description": "Nombre del producto, p. ej. laptop"}},
            "required": ["producto"],
        },
    },
    {
        "name": "calcular_precio",
        "description": "Calcula el precio total en soles de comprar una cantidad de un producto.",
        "input_schema": {
            "type": "object",
            "properties": {
                "producto": {"type": "string", "description": "Nombre del producto"},
                "cantidad": {"type": "integer", "description": "Número de unidades"},
            },
            "required": ["producto", "cantidad"],
        },
    },
]


def main() -> None:
    pedido = sys.argv[1] if len(sys.argv) > 1 else "Quiero 3 laptops. ¿Hay stock y cuánto pagaría?"
    print(f"Cliente: {pedido}\n")

    cliente = anthropic.Anthropic()
    mensajes = [{"role": "user", "content": pedido}]

    # -----------------------------------------------------------------------
    # 3. El bucle del agente: percibir -> razonar -> actuar -> repetir
    # -----------------------------------------------------------------------
    for paso in range(1, MAX_PASOS + 1):
        respuesta = cliente.beta.messages.create(
            model=MODELO,
            max_tokens=16000,
            system="Eres el vendedor de una tienda de tecnología en Lima. Usa las herramientas antes de responder. Responde en español.",
            tools=HERRAMIENTAS,
            messages=mensajes,
            **OPCIONES,
        )

        if respuesta.stop_reason == "refusal":
            print("El modelo no respondió esta solicitud por motivos de seguridad.")
            return

        if respuesta.stop_reason == "pause_turn":  # el servidor pausó el turno: lo reenviamos
            mensajes.append({"role": "assistant", "content": respuesta.content})
            continue

        # Guardamos la respuesta completa del modelo en la conversación
        mensajes.append({"role": "assistant", "content": respuesta.content})

        if respuesta.stop_reason != "tool_use":  # "end_turn": el agente terminó
            texto = "".join(b.text for b in respuesta.content if b.type == "text")
            print(f"Agente: {texto}")
            return

        # El modelo pidió una o más herramientas: las ejecutamos todas
        resultados = []
        for bloque in respuesta.content:
            if bloque.type == "tool_use":
                print(f"Paso {paso}: el agente usa {bloque.name}({json.dumps(bloque.input, ensure_ascii=False)})")
                funcion = FUNCIONES[bloque.name]
                salida = funcion(**bloque.input)
                print(f"         resultado -> {salida}")
                resultados.append({"type": "tool_result", "tool_use_id": bloque.id, "content": salida})

        # Todos los resultados van juntos en UN solo mensaje del usuario
        mensajes.append({"role": "user", "content": resultados})

    print(f"Se alcanzó el máximo de {MAX_PASOS} pasos sin respuesta final.")


if __name__ == "__main__":
    try:
        main()
    except anthropic.AuthenticationError:
        print("Clave de API inválida o ausente. Revise el archivo .env (vea el README).")
    except anthropic.APIConnectionError:
        print("No hay conexión con la API. Revise su internet.")
