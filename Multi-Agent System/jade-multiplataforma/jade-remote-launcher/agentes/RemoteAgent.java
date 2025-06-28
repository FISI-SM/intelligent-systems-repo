
package agentes;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class RemoteAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + ": esperando mensajes...");
        addBehaviour(new jade.core.behaviours.CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(getLocalName() + " recibió: " + msg.getContent());
                } else {
                    block();
                }
            }
        });
    }
}
