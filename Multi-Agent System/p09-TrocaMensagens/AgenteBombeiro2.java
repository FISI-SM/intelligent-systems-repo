/**
 * Ejemplo de ejecución:
 * java jade.Boot -gui -agents "Bombeiro:AgenteBombeiro2;A1:AgenteAlarmado2"
 */
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteBombeiro2 extends Agent {
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
