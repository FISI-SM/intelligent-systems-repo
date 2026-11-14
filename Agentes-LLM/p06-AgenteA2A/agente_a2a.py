"""
Archivo:      agente_a2a.py

Propósito:    Publicar un agente LLM con el protocolo A2A (Agent2Agent). El
              agente "Tutor de Algoritmos" usa Claude para explicar temas de
              algoritmos. Otros agentes lo descubren leyendo su Agent Card y
              le envían tareas por HTTP (JSON-RPC).

Ejecución (con el entorno virtual activado, desde la carpeta p06-AgenteA2A;
igual en Windows CMD, PowerShell, Linux y macOS, incluido Apple Silicon M1):
       python agente_a2a.py
       Luego, en OTRA terminal (también con el entorno activado):
       python cliente_a2a.py
       Para ver la Agent Card en el navegador:
       http://127.0.0.1:9001/.well-known/agent-card.json

Entrada:      Tareas A2A enviadas por un cliente (texto del usuario).

Salida:       En la terminal: un aviso por cada tarea recibida. Al cliente:
              los estados de la tarea (SUBMITTED, WORKING, COMPLETED) y un
              artefacto con la respuesta.

Notas:
   1. Agent Card = la "tarjeta de presentación" del agente: nombre,
      descripción, habilidades (skills) y dirección. Es el equivalente
      moderno de registrarse en las Páginas Amarillas (DF) de JADE.
   2. AgentExecutor.execute() es el equivalente al comportamiento (Behaviour)
      de JADE: aquí está la lógica del agente.
   3. TaskUpdater publica los cambios de estado de la tarea; el resultado
      final se entrega como artefacto (Artifact).
   4. El agente se detiene con Ctrl+C.
   5. a2a-sdk 1.x usa tipos protobuf (Part(text=...), TaskState.TASK_STATE_*);
      los tutoriales de la versión 0.x usan otros nombres.
"""

import anthropic
import uvicorn
from dotenv import load_dotenv
from starlette.applications import Starlette

from a2a.server.agent_execution import AgentExecutor, RequestContext
from a2a.server.events import EventQueue
from a2a.server.request_handlers import DefaultRequestHandler
from a2a.server.routes import create_agent_card_routes, create_jsonrpc_routes
from a2a.server.tasks import InMemoryTaskStore, TaskUpdater
from a2a.types import (
    AgentCapabilities,
    AgentCard,
    AgentInterface,
    AgentSkill,
    Part,
    Task,
    TaskState,
    TaskStatus,
)

load_dotenv()

MODELO = "claude-opus-5-5"
OPCIONES = {"betas": ["server-side-fallback-2026-07-01"], "fallbacks": "default"}
PUERTO = 9001

# ---------------------------------------------------------------------------
# 1. La Agent Card: cómo se presenta el agente ante los demás
# ---------------------------------------------------------------------------
TARJETA = AgentCard(
    name="Tutor de Algoritmos",
    description="Explica algoritmos y su complejidad con ejemplos sencillos, en español.",
    version="1.0.0",
    capabilities=AgentCapabilities(streaming=True),
    default_input_modes=["text"],
    default_output_modes=["text"],
    skills=[
        AgentSkill(
            id="explicar_algoritmo",
            name="Explicar un algoritmo",
            description="Explica cómo funciona un algoritmo y su complejidad Big-O.",
            tags=["algoritmos", "complejidad", "educación"],
            examples=["¿Cómo funciona la búsqueda binaria?", "¿Por qué quicksort es O(n log n)?"],
        )
    ],
    supported_interfaces=[
        AgentInterface(protocol_binding="JSONRPC", protocol_version="1.0", url=f"http://127.0.0.1:{PUERTO}/")
    ],
)


# ---------------------------------------------------------------------------
# 2. La lógica del agente: recibe una tarea, usa el LLM y entrega el resultado
# ---------------------------------------------------------------------------
class TutorExecutor(AgentExecutor):
    def __init__(self) -> None:
        self.llm = anthropic.AsyncAnthropic()

    async def execute(self, context: RequestContext, event_queue: EventQueue) -> None:
        # a) Registrar la tarea nueva (estado SUBMITTED)
        await event_queue.enqueue_event(
            Task(
                id=context.task_id,
                context_id=context.context_id,
                status=TaskStatus(state=TaskState.TASK_STATE_SUBMITTED),
                history=[context.message],
            )
        )
        tarea = TaskUpdater(event_queue=event_queue, task_id=context.task_id, context_id=context.context_id)

        # b) Pasar a WORKING mientras el LLM piensa
        pregunta = context.get_user_input()
        print(f"[Tutor] Tarea recibida: {pregunta}", flush=True)
        await tarea.start_work(tarea.new_agent_message(parts=[Part(text="Pensando la explicación...")]))

        try:
            respuesta = await self.llm.beta.messages.create(
                model=MODELO,
                max_tokens=16000,
                system="Eres un tutor de Análisis y Diseño de Algoritmos de la FISI-UNMSM. Explica en español, en máximo 8 líneas.",
                messages=[{"role": "user", "content": pregunta}],
                **OPCIONES,
            )
            texto = "".join(b.text for b in respuesta.content if b.type == "text")
        except anthropic.APIError as error:
            # c) Si el LLM falla, la tarea termina en estado FAILED
            await tarea.failed(tarea.new_agent_message(parts=[Part(text=f"Error del LLM: {error}")]))
            return

        # d) Entregar el resultado como artefacto y cerrar la tarea (COMPLETED)
        await tarea.add_artifact(parts=[Part(text=texto)], name="explicacion")
        await tarea.complete()

    async def cancel(self, context: RequestContext, event_queue: EventQueue) -> None:
        tarea = TaskUpdater(event_queue=event_queue, task_id=context.task_id, context_id=context.context_id)
        await tarea.cancel()


# ---------------------------------------------------------------------------
# 3. El servidor web: publica la Agent Card y recibe tareas por JSON-RPC
# ---------------------------------------------------------------------------
manejador = DefaultRequestHandler(agent_executor=TutorExecutor(), task_store=InMemoryTaskStore(), agent_card=TARJETA)
app = Starlette(
    routes=[
        *create_agent_card_routes(agent_card=TARJETA),  # GET /.well-known/agent-card.json
        *create_jsonrpc_routes(request_handler=manejador, rpc_url="/"),  # POST / (tareas)
    ]
)

if __name__ == "__main__":
    print(f"Tutor de Algoritmos escuchando en http://127.0.0.1:{PUERTO}  (Ctrl+C para detener)", flush=True)
    uvicorn.run(app, host="127.0.0.1", port=PUERTO, log_level="warning")
