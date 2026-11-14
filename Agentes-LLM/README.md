# Agentes LLM con MCP y A2A

Ejemplos prácticos, en Python, para construir **agentes basados en LLM**, conectarlos a herramientas con **MCP** (Model Context Protocol) y hacer que se comuniquen entre sí con **A2A** (Agent2Agent).

Es la continuación de la carpeta `Multi-Agent System` (JADE): los conceptos son los mismos (agente, comportamiento, mensajes, páginas amarillas), pero con la tecnología actual.

## Ejemplos

| Carpeta | Qué aprende | Archivos |
|---|---|---|
| `p01-PrimeraLlamada` | Un LLM solo responde texto: todavía no es un agente | `primera_llamada.py` |
| `p02-BucleDelAgente` | El bucle del agente escrito a mano: pensar, usar herramienta, observar, repetir | `bucle_del_agente.py` |
| `p03-ToolRunner` | El mismo agente, pero el SDK hace el bucle | `tool_runner.py` |
| `p04-ServidorMCP` | Crear un servidor MCP con herramientas y un recurso, y probarlo sin LLM | `servidor_biblioteca.py`, `cliente_prueba.py` |
| `p05-AgenteConMCP` | Un agente LLM que descubre y usa las herramientas del servidor MCP | `agente_con_mcp.py` |
| `p06-AgenteA2A` | Publicar un agente con A2A (Agent Card + tareas) y hablarle desde un cliente | `agente_a2a.py`, `cliente_a2a.py` |
| `p07-SistemaMultiagente` | Coordinador + 3 especialistas (Bombero, Policía, Médico) por A2A | `agentes_emergencia.py`, `coordinador.py` |

Cada archivo empieza con un comentario que explica su propósito, cómo ejecutarlo, la entrada, la salida y notas.

## De JADE a agentes LLM

| JADE (`Multi-Agent System`) | Agentes LLM (esta carpeta) |
|---|---|
| `Agent` + `setup()` | LLM + instrucciones (system prompt) |
| `Behaviour` (`action()` / `done()`) | Bucle del agente (p02, p03) |
| Lógica con `if/else` | El LLM decide qué hacer |
| Métodos del agente | Herramientas (tools) y servidores MCP (p04, p05) |
| Registro en el DF (`DFService.register`) | Agent Card en `/.well-known/agent-card.json` (p06) |
| Búsqueda en el DF (`DFService.search`) | Leer las Agent Cards de un directorio (p07) |
| `ACLMessage` (INFORM, REQUEST) | Tarea A2A (SUBMITTED, WORKING, COMPLETED) |
| p08-PaginasAmarillas | p07-SistemaMultiagente |

## Instalación (una sola vez)

Necesita **Python 3.10 o superior** y una **clave de API de Claude** (https://platform.claude.com, sección API Keys). Todos los comandos se escriben **desde esta carpeta** (`Agentes-LLM`).

### 1. Crear el entorno virtual

| Sistema | Comando |
|---|---|
| Windows (CMD o PowerShell) | `py -m venv .venv` |
| Linux / macOS (Intel y Apple Silicon M1) | `python3 -m venv .venv` |

### 2. Activar el entorno virtual

Hágalo **cada vez** que abra una terminal nueva. Al activarlo, la línea de comandos empieza con `(.venv)`.

| Sistema | Comando |
|---|---|
| Windows CMD | `.venv\Scripts\activate.bat` |
| Windows PowerShell | `.venv\Scripts\Activate.ps1` |
| Linux / macOS | `source .venv/bin/activate` |

Si PowerShell dice que la ejecución de scripts está deshabilitada, ejecute primero (solo afecta a esa ventana):

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
```

### 3. Instalar las dependencias

Con el entorno activado (igual en todos los sistemas):

```
pip install -r requirements.txt
```

### 4. Guardar la clave de API

Copie `.env.example` como `.env` y pegue su clave en lugar de `pegue-aqui-su-clave`.

| Sistema | Comando para copiar |
|---|---|
| Windows CMD | `copy .env.example .env` |
| Windows PowerShell | `Copy-Item .env.example .env` |
| Linux / macOS | `cp .env.example .env` |

El archivo `.env` está en `.gitignore`: **nunca suba su clave a GitHub**.

## Ejecutar un ejemplo

Con el entorno activado, entre a la carpeta del ejemplo y ejecute el archivo con `python` (dentro del entorno virtual, `python` funciona igual en Windows, Linux y macOS):

```
cd p02-BucleDelAgente
python bucle_del_agente.py
python bucle_del_agente.py "¿Me alcanza el stock para 4 mouse?"
```

Los ejemplos p06 y p07 necesitan **dos terminales**: en la primera se inicia el agente (servidor) y en la segunda el cliente. Active el entorno virtual en ambas.

## Bibliotecas usadas

Se usan los **SDK oficiales** de cada pieza, sin frameworks intermedios, para aprender los protocolos tal como son:

| Biblioteca | Versión | Para qué |
|---|---|---|
| `anthropic` | 2.8.0 | Llamar al LLM (Claude), herramientas y Tool Runner |
| `mcp` | 2.3.0 | Servidor y cliente MCP |
| `a2a-sdk` | 1.3.0 | Servidor y cliente A2A |

Atención con tutoriales antiguos de internet:

- **mcp 2.x** renombró `FastMCP` como `MCPServer` (`from mcp.server.mcpserver import MCPServer`).
- **a2a-sdk 1.x** usa tipos protobuf (`Part(text=...)`, `TaskState.TASK_STATE_COMPLETED`); el código de la versión 0.x no funciona.

## Modelo y costo

Los ejemplos usan el modelo `claude-opus-5-5` (constante `MODELO` al inicio de cada archivo). Cada ejecución consume unos pocos miles de tokens; el costo depende del modelo (vea https://platform.claude.com/docs/en/about-claude/pricing). Para prácticas con muchos alumnos puede cambiar `MODELO` por un modelo más económico, por ejemplo `claude-haiku-5-5`.

Los ejemplos activan `fallbacks`: si el modelo rechaza una solicitud por seguridad, la API la reintenta con otro modelo adecuado.

## Problemas frecuentes

| Mensaje | Solución |
|---|---|
| `Clave de API inválida o ausente` | Revise que `.env` exista en la carpeta `Agentes-LLM` y que la clave esté bien copiada |
| `ModuleNotFoundError: No module named 'anthropic'` | Active el entorno virtual e instale `requirements.txt` |
| `No se pudo conectar con http://127.0.0.1:9001` | Inicie primero el agente (p06: `agente_a2a.py`; p07: `agentes_emergencia.py`) en otra terminal |
| `address already in use` / puerto ocupado | Ya hay un agente corriendo en ese puerto: ciérrelo con Ctrl+C |
| `No module named 'mcp.server.fastmcp'` | Está usando código de mcp 1.x; use `MCPServer` (vea arriba) |
