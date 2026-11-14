/*
 * Archivo:      AgenteBombero2.java
 *
 * Propósito:    Mostrar cómo un agente responde a un mensaje ACL con
 *               createReply(): cuando recibe "Fuego", agradece al remitente y
 *               avisa que combatirá el incendio.
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
 *       java -cp .:../jade.jar jade.Boot -agents "Bombero:AgenteBombero2;A1:AgenteAlarmado2"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "Bombero:AgenteBombero2;A1:AgenteAlarmado2"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "Bombero:AgenteBombero2;A1:AgenteAlarmado2"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Mensajes ACL; reacciona al contenido "Fuego".
 *
 * Salida:       "El agente A1@<host>:1099/JADE avisó de un incendio." y "¡Voy
 *               a activar los procedimientos de combate al incendio!". La
 *               respuesta la imprime AgenteAlarmado2.
 *
 * Notas:
 *    1. createReply() copia el remitente como receptor y la conversación del
 *       mensaje original.
 *    2. El nombre local debe ser Bombero.
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
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteBombero2 extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    ACLMessage reply = msg.createReply();
                    String content = msg.getContent();

                    if (content.equalsIgnoreCase("Fuego")) {
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("¡Recibí tu aviso! Gracias por ayudar en mi servicio.");
                        myAgent.send(reply);

                        System.out.println("El agente " + msg.getSender().getName() + " avisó de un incendio.");
                        System.out.println("¡Voy a activar los procedimientos de combate al incendio!");
                    }
                } else {
                    block(); // se suspende hasta que llegue un nuevo mensaje
                }
            }
        });
    }
}
