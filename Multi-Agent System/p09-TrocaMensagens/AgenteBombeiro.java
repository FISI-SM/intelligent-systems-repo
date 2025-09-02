/**
 * Ejemplo de ejecución:
 * java jade.Boot -gui -agents "Bombeiro:AgenteBombeiro;A1:AgenteAlarmado"
 */
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteBombeiro extends Agent {
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
