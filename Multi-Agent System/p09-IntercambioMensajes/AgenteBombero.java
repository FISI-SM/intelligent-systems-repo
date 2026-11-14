/*
 * Archivo:      AgenteBombero.java
 *
 * Propósito:    Mostrar cómo un agente recibe mensajes ACL con un
 *               CyclicBehaviour y reacciona cuando el contenido es "Fuego".
 *
 * Compilación (desde la carpeta p09-IntercambioMensajes; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "Bombero:AgenteBombero;A1:AgenteAlarmado"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "Bombero:AgenteBombero;A1:AgenteAlarmado"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "Bombero:AgenteBombero;A1:AgenteAlarmado"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Mensajes ACL; reacciona al contenido "Fuego" (sin importar
 *               mayúsculas).
 *
 * Salida:       "El agente A1@<host>:1099/JADE avisó de un incendio." y "¡Voy
 *               a activar los procedimientos de combate al incendio!".
 *
 * Notas:
 *    1. El nombre local debe ser Bombero: AgenteAlarmado envía su mensaje a
 *       ese nombre.
 *    2. block() suspende el comportamiento hasta que llegue otro mensaje, sin
 *       consumir CPU.
 *    3. AgenteBombero2.java es la versión que también responde al remitente.
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
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteBombero extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {

                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    String content = msg.getContent(); 
                    // equalsIgnoreCase hace la comparación sin distinguir mayúsculas/minúsculas
                    if (content.equalsIgnoreCase("Fuego")) {
                        System.out.println("El agente " + msg.getSender().getName() + " avisó de un incendio.");
                        System.out.println("¡Voy a activar los procedimientos de combate al incendio!");
                    }
                } else {
                    block(); // suspende hasta que llegue un nuevo mensaje
                }
            } // fin de action()
        }); // fin de addBehaviour()
    }
}
