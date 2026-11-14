/*
 * Archivo:      AgenteAlarmado2.java
 *
 * Propósito:    Mostrar un agente con dos comportamientos: uno
 *               (OneShotBehaviour) envía "Fuego" al Bombero y otro
 *               (CyclicBehaviour) imprime las respuestas que recibe.
 *
 * Compilación (desde la carpeta p09-IntercambioMensages; compila todos los .java):
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
 * Entrada:      Ninguna.
 *
 * Salida:       "AgenteAlarmado2: ¡Mensaje de emergencia enviado al Bombero!"
 *               y "--> Bombero@<host>:1099/JADE: ¡Recibí tu aviso! Gracias
 *               por ayudar en mi servicio.".
 *
 * Notas:
 *    1. Un agente puede tener varios comportamientos; JADE los ejecuta por
 *       turnos en un solo hilo.
 *    2. La plataforma JADE sigue activa después del agente; deténgala con
 *       Ctrl+C.
 *    3. El separador del classpath es ':' en Linux/macOS y ';' en Windows. En
 *       PowerShell el classpath va entre comillas, porque ';' separa comandos.
 *    4. -encoding UTF-8 evita errores con tildes y ñ al compilar en Windows
 *       con JDK 17 o anterior.
 *    5. En Mac M1 use un JDK nativo arm64 (por ejemplo, Temurin aarch64). JADE
 *       es Java puro y no necesita nada especial. Probado con JDK 17 y JADE
 *       4.5.0.
 */

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class AgenteAlarmado2 extends Agent {
    protected void setup() {
        // Comportamiento de una sola ejecución: envía un mensaje al agente Bombero
        addBehaviour(new OneShotBehaviour(this) {
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("Bombero", AID.ISLOCALNAME));
                msg.setLanguage("Portugués");
                msg.setOntology("Emergencia");
                msg.setContent("Fuego");

                myAgent.send(msg);
                System.out.println("AgenteAlarmado2: ¡Mensaje de emergencia enviado al Bombero!");
            }
        });

        // Comportamiento cíclico: queda a la espera de respuestas o mensajes entrantes
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println("--> " + msg.getSender().getName() + ": " + content);
                } else {
                    // Con block() el comportamiento queda suspendido hasta que llegue un nuevo mensaje
                    // Esto evita consumir ciclos de CPU innecesariamente
                    block();
                }
            }
        });
    }
}
