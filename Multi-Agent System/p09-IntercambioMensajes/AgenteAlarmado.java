/*
 * Archivo:      AgenteAlarmado.java
 *
 * Propósito:    Mostrar cómo un agente crea y envía un mensaje ACL de tipo
 *               INFORM, con contenido "Fuego", al agente llamado Bombero.
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
 * Entrada:      Ninguna.
 *
 * Salida:       "AgenteAlarmado: ¡Mensaje de emergencia enviado al Bombero!".
 *               La reacción la imprime el AgenteBombero.
 *
 * Notas:
 *    1. new AID("Bombero", AID.ISLOCALNAME) identifica al receptor por su
 *       nombre local.
 *    2. El idioma (setLanguage) y la ontología (setOntology) son metadatos; el
 *       receptor de este ejemplo no los revisa.
 *    3. La plataforma JADE sigue activa después del agente; deténgala con
 *       Ctrl+C.
 *    4. El separador del classpath es ':' en Linux/macOS y ';' en Windows. En
 *       PowerShell el classpath va entre comillas, porque ';' separa comandos.
 *    5. -encoding UTF-8 evita errores con tildes y ñ al compilar en Windows
 *       con JDK 17 o anterior.
 *    6. En Mac M1 use un JDK nativo arm64 (por ejemplo, Temurin aarch64). JADE
 *       es Java puro y no necesita nada especial. Probado con JDK 17 y JADE
 *       4.5.0.
 */

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class AgenteAlarmado extends Agent {
    protected void setup() {
        addBehaviour(new OneShotBehaviour(this) {
            public void action() {
                // Crea un mensaje ACL de tipo INFORM
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

                // Define el receptor: el agente llamado "Bombero"
                msg.addReceiver(new AID("Bombero", AID.ISLOCALNAME));

                // Define metadatos del mensaje
                msg.setLanguage("Español");
                msg.setOntology("Emergencia");

                // Define el contenido del mensaje
                msg.setContent("Fuego");

                // Envía el mensaje
                myAgent.send(msg);

                System.out.println("AgenteAlarmado: ¡Mensaje de emergencia enviado al Bombero!");
            }
        });
    }
}
