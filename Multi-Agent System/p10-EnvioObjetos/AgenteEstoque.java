/** Código 3.8: AgenteEstoque.java */
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import java.io.IOException;

public class AgenteEstoque extends Agent {
    Musicos[] mus = new Musicos[5];

    protected void setup() {
        // Inicializamos el arreglo de músicos
        mus[0] = new Musicos("Claudia Leite", 30, "Babado Novo");
        mus[1] = new Musicos("Paula Toller", 45, "Kid Abelha");
        mus[2] = new Musicos("Rogerio Flausino", 37, "Jota Quest");
        mus[3] = new Musicos("Laura Pausini", 33, null);
        mus[4] = new Musicos("Bono Vox", 47, "U2");

        addBehaviour(new SimpleBehaviour(this) { // inicio del comportamiento

            int cont = 0;

            public void action() {
                try {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID("Contador", AID.ISLOCALNAME));
                    msg.setContentObject(mus[cont]);
                    myAgent.send(msg); // envía el mensaje con el objeto Musicos

                    cont = cont + 1;
                    // block(100); // se podría usar para espaciar los envíos
                } catch (IOException ex) {
                    System.out.println("Error en el envío del mensaje");
                }
            }

            public boolean done() {
                if (cont > 4) {
                    myAgent.doDelete(); // finaliza el agente
                    return true;
                } else {
                    return false;
                }
            }
        }); // fin del comportamiento
    } // fin del método setup() del agente

    // La invocación del método doDelete() dispara la ejecución de takeDown()
    protected void takeDown() {
        System.out.println("Toda la información fue enviada");
    }
}
