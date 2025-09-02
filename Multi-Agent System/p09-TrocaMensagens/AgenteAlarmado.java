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

                // Define el receptor: el agente llamado "Bombeiro"
                msg.addReceiver(new AID("Bombeiro", AID.ISLOCALNAME));

                // Define metadatos del mensaje
                msg.setLanguage("Portugués");
                msg.setOntology("Emergencia");

                // Define el contenido del mensaje
                msg.setContent("Fuego");

                // Envía el mensaje
                myAgent.send(msg);

                System.out.println("AgenteAlarmado: ¡Mensaje de emergencia enviado al Bombeiro!");
            }
        });
    }
}
