"""
Archivo:      agentes_emergencia.py

Propósito:    Iniciar tres agentes especialistas A2A (Bombero, Policía y
              Médico), cada uno con su propio LLM, su Agent Card y su puerto.
              Es la versión moderna del laboratorio p08-PaginasAmarillas de
              JADE: cada agente publica su "servicio" en su Agent Card.

Ejecución (con el entorno virtual activado, desde la carpeta
p07-SistemaMultiagente; igual en Windows CMD, PowerShell, Linux y macOS,
incluido Apple Silicon M1):
       python agentes_emergencia.py
       Luego, en OTRA terminal (también con el entorno activado):
       python coordinador.py

Entrada:      Tareas A2A enviadas por el coordinador.

Salida:       En la terminal: un aviso por cada tarea que recibe cada agente.
              Al coordinador: el plan de acción del especialista (artefacto).

Notas:
   1. Los tres agentes usan la MISMA clase (Especialista); solo cambian la
      Agent Card y las instrucciones (system prompt). En JADE eran tres
      clases casi iguales (Bombero, Medico, Policia).
   2. Puertos: Bombero 9201, Policía 9202, Médico 9203. Vea sus tarjetas en
      http://127.0.0.1:9201/.well-known/agent-card.json (y 9202, 9203).
   3. Los tres servidores corren en un solo proceso con asyncio.gather, para
      no tener que abrir tres terminales.
   4. Detenga los agentes con Ctrl+C.
"""

import asyncio
from dataclasses import dataclass

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


@dataclass
class Perfil:
    """Datos que distinguen a un especialista de otro."""

    nombre: str
    puerto: int
    servicio: str  # lo que publica en su Agent Card (como el "tipo de servicio" del DF)
    instrucciones: str  # system prompt de su LLM
    ejemplos: list[str]


PERFILES = [
    Perfil(
        "Bombero", 9201,
        "Atiende incendios, fugas de gas, humo y rescates en estructuras.",
        "Eres el agente Bombero de Lima. Recibes un aviso y respondes con un plan de acción de máximo 3 pasos.",
        ["Hay humo saliendo de un edificio", "Huele a gas en la cocina"],
    ),
    Perfil(
        "Policía", 9202,
        "Atiende robos, asaltos, accidentes de tránsito y control del orden público.",
        "Eres el agente Policía de Lima. Recibes un aviso y respondes con un plan de acción de máximo 3 pasos.",
        ["Me robaron el celular", "Hubo un choque en la avenida"],
    ),
    Perfil(
        "Médico", 9203,
        "Atiende personas heridas, enfermas o con malestar; da primeros auxilios.",
        "Eres el agente Médico (SAMU) de Lima. Recibes un aviso y respondes con un plan de primeros auxilios de máximo 3 pasos.",
        ["Una persona se desmayó", "Hay un herido sangrando"],
    ),
]


def crear_tarjeta(p: Perfil) -> AgentCard:
    """La Agent Card de un especialista: su registro en las 'páginas amarillas'."""
    return AgentCard(
        name=p.nombre,
        description=p.servicio,
        version="1.0.0",
        capabilities=AgentCapabilities(streaming=True),
        default_input_modes=["text"],
        default_output_modes=["text"],
        skills=[AgentSkill(id=f"atender_{p.puerto}", name=f"Atender emergencias ({p.nombre})",
                           description=p.servicio, tags=["emergencias", "lima"], examples=p.ejemplos)],
        supported_interfaces=[
            AgentInterface(protocol_binding="JSONRPC", protocol_version="1.0", url=f"http://127.0.0.1:{p.puerto}/")
        ],
    )


class Especialista(AgentExecutor):
    """Lógica común de los tres especialistas: recibir aviso -> LLM -> plan."""

    def __init__(self, perfil: Perfil) -> None:
        self.perfil = perfil
        self.llm = anthropic.AsyncAnthropic()

    async def execute(self, context: RequestContext, event_queue: EventQueue) -> None:
        await event_queue.enqueue_event(
            Task(id=context.task_id, context_id=context.context_id,
                 status=TaskStatus(state=TaskState.TASK_STATE_SUBMITTED), history=[context.message])
        )
        tarea = TaskUpdater(event_queue=event_queue, task_id=context.task_id, context_id=context.context_id)
        aviso = context.get_user_input()
        print(f"[{self.perfil.nombre}] Aviso recibido: {aviso}", flush=True)
        await tarea.start_work()

        try:
            respuesta = await self.llm.beta.messages.create(
                model=MODELO,
                max_tokens=16000,
                system=self.perfil.instrucciones + " Responde en español.",
                messages=[{"role": "user", "content": aviso}],
                **OPCIONES,
            )
            plan = "".join(b.text for b in respuesta.content if b.type == "text")
        except anthropic.APIError as error:
            await tarea.failed(tarea.new_agent_message(parts=[Part(text=f"Error del LLM: {error}")]))
            return

        await tarea.add_artifact(parts=[Part(text=plan)], name="plan_de_accion")
        await tarea.complete()

    async def cancel(self, context: RequestContext, event_queue: EventQueue) -> None:
        await TaskUpdater(event_queue=event_queue, task_id=context.task_id, context_id=context.context_id).cancel()


def crear_servidor(p: Perfil) -> uvicorn.Server:
    tarjeta = crear_tarjeta(p)
    manejador = DefaultRequestHandler(agent_executor=Especialista(p), task_store=InMemoryTaskStore(), agent_card=tarjeta)
    app = Starlette(routes=[*create_agent_card_routes(agent_card=tarjeta),
                            *create_jsonrpc_routes(request_handler=manejador, rpc_url="/")])
    return uvicorn.Server(uvicorn.Config(app, host="127.0.0.1", port=p.puerto, log_level="warning"))


async def main() -> None:
    for p in PERFILES:
        print(f"{p.nombre:8} -> http://127.0.0.1:{p.puerto}  ({p.servicio})", flush=True)
    print("Agentes listos. Ctrl+C para detener.\n", flush=True)
    await asyncio.gather(*(crear_servidor(p).serve() for p in PERFILES))


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("Agentes detenidos.")
