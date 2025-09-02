import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class Solicitante extends Agent {
    protected void setup() {
        // Captura argumentos
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            String argumento = (String) args[0];

            // Si el argumento es "fuego"
            if (argumento.equalsIgnoreCase("fogo")) {
                ServiceDescription servicio = new ServiceDescription();
                // El servicio es apagar fuego
                servicio.setType("apaga fuego");
                // Busca quién ofrece ese servicio
                buscar(servicio, "fuego");
            }

            // Si el argumento es "ladrón"
            if (argumento.equalsIgnoreCase("ladrao")) {
                ServiceDescription servicio = new ServiceDescription();
                // El servicio es atrapar ladrones
                servicio.setType("prende ladrón");
                buscar(servicio, "ladrón");
            }

            // Si el argumento es "enfermo"
            if (argumento.equalsIgnoreCase("doente")) {
                ServiceDescription servicio = new ServiceDescription();
                // El servicio es salvar vidas
                servicio.setType("salva vidas");
                buscar(servicio, "enfermo");
            }

            // Comportamiento para recibir mensajes de respuesta
            addBehaviour(new CyclicBehaviour(this) {
                public void action() {
                    ACLMessage msg = receive();
                    if (msg != null)
                        System.out.println(msg.getSender() + " → " + msg.getContent());
                    else
                        block();
                }
            });
        }
    }

    // Método que realiza la búsqueda en las Páginas Amarillas (DF) de la plataforma
    protected void buscar(final ServiceDescription sd, final String pedido) {
        // Cada minuto intenta buscar agentes que ofrezcan el servicio
        addBehaviour(new TickerBehaviour(this, 60000) {
            protected void onTick() {
                DFAgentDescription dfd = new DFAgentDescription();
                dfd.addServices(sd);

                try {
                    DFAgentDescription[] resultado = DFService.search(myAgent, dfd);
                    if (resultado.length != 0) {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(resultado[0].getName());
                        msg.setContent(pedido);
                        myAgent.send(msg);
                        stop(); // Finaliza el comportamiento cuando encuentra y contacta
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
