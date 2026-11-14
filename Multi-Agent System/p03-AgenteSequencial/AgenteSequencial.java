/*
 * Archivo:      AgenteSequencial.java
 *
 * Propósito:    Mostrar SequentialBehaviour: un comportamiento que ejecuta
 *               sus subcomportamientos uno tras otro. Aquí son un
 *               WakerBehaviour (espera 500 ms), un OneShotBehaviour y un
 *               TickerBehaviour (cada 2000 ms, tres veces).
 *
 * Compilación (desde la carpeta p03-AgenteSequencial; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "seq:AgenteSequencial"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "seq:AgenteSequencial"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "seq:AgenteSequencial"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Ninguna.
 *
 * Salida:       El saludo, "~500: ¡Ejecuté mi primer comportamiento...",
 *               "Ejecuté mi segundo comportamiento", tres líneas "Estoy
 *               ejecutando mi tercer comportamiento" (a ~2000, ~4000 y ~6000
 *               ms) y "Fui finalizado con éxito".
 *
 * Notas:
 *    1. Un subcomportamiento empieza solo cuando el anterior termina.
 *    2. El TickerBehaviour termina con stop(); entonces onEnd() del
 *       SequentialBehaviour llama a doDelete(), y JADE ejecuta takeDown().
 *    3. Los tiempos impresos son aproximados (milisegundos desde que se creó
 *       cada subcomportamiento).
 *    4. La plataforma JADE sigue activa después del agente; deténgala con
 *       Ctrl+C.
 *    5. El separador del classpath es ':' en Linux/macOS y ';' en Windows. En
 *       PowerShell el classpath va entre comillas, porque ';' separa comandos.
 *    6. -encoding UTF-8 evita errores con tildes y ñ al compilar en Windows
 *       con JDK 17 o anterior.
 *    7. En Mac M1 use un JDK nativo arm64 (por ejemplo, Temurin aarch64). JADE
 *       es Java puro y no necesita nada especial. Probado con JDK 17 y JADE
 *       4.5.0.
 */

import jade.core.Agent;
import jade.core.behaviours.*;

public class AgenteSequencial extends Agent {
	
	protected void setup() {
		// mensaje de inicialización del agente
		System.out.println("¡Hola! Mi nombre es " + getLocalName());
		System.out.println("Voy a ejecutar tres comportamientos:");

		// creamos un objeto de la clase SequentialBehaviour
		SequentialBehaviour comportamiento = new SequentialBehaviour(this) {
			public int onEnd() {
				myAgent.doDelete();
				return 0;
			}
		};

		// añadimos su primer comportamiento
		comportamiento.addSubBehaviour(new WakerBehaviour(this, 500) {
			long t0 = System.currentTimeMillis();
			protected void onWake() {
				System.out.println((System.currentTimeMillis() - t0) + 
				": ¡Ejecuté mi primer comportamiento después de medio segundo!");
			}
		});

		// añadimos su segundo comportamiento
		comportamiento.addSubBehaviour(new OneShotBehaviour(this) {
			public void action() {
				System.out.println("Ejecuté mi segundo comportamiento");
			}
		});

		// añadimos su tercer comportamiento
		comportamiento.addSubBehaviour(new TickerBehaviour(this, 2000) {
			int exec = 0;
			long t1 = System.currentTimeMillis();
			protected void onTick() {
				if (exec == 3) stop();
				else {
					System.out.println((System.currentTimeMillis() - t1) + 
					": Estoy ejecutando mi tercer comportamiento");
					exec++;
				}
			}
		});

		// activamos su ejecución
		addBehaviour(comportamiento);
	}

	protected void takeDown() {
		System.out.println("Fui finalizado con éxito");
	}
}
