/**
 * $ javac -cp jade.jar agentes/MainAgent.java
 * $ java -cp jade.jar:. jade.Boot -gui MainAgent:agentes.MainAgent
 *  
*/
package agentes;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class MainAgent extends Agent {
    @Override
    protected void setup() {

        System.out.println(getLocalName() + ": iniciado.");

        // Esperamos unos segundos para asegurarnos que el agente remoto esté listo
        try {
            Thread.sleep(15000); // segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("RemoteAgent", AID.ISLOCALNAME));
        msg.setContent("Hola desde MainAgent (fuera de Docker)!");
        send(msg);
        System.out.println(getLocalName() + ": mensaje enviado.");
    }
}
