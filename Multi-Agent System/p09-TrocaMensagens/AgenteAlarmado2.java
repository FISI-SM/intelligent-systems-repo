import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class AgenteAlarmado2 extends Agent {
    protected void setup() {
        // Comportamiento de una sola ejecución: envía un mensaje al agente Bombeiro
        addBehaviour(new OneShotBehaviour(this) {
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("Bombeiro", AID.ISLOCALNAME));
                msg.setLanguage("Portugués");
                msg.setOntology("Emergencia");
                msg.setContent("Fuego");

                myAgent.send(msg);
                System.out.println("AgenteAlarmado2: ¡Mensaje de emergencia enviado al Bombeiro!");
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
