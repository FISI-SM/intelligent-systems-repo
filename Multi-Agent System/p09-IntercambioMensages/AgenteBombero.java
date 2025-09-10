/**
 * ------------------------------------------------------------
 *   Solicitante: Ejemplo para mostrar el uso de páginas amarillas
 *
 * Compilar y ejecutar en Linux:
 * $ javac -d out -cp "../jade.jar" *.java
 * $ java -cp ".:../jade.jar:out" jade.Boot -gui -agents 'Bombero:AgenteBombero; A1:AgenteAlarmado'
 * 
 * Compilar en Windows:
 * $ javac -d out -cp "..\\jade.jar" *.java 
 * $ 
 *
 * ------------------------------------------------------------
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
