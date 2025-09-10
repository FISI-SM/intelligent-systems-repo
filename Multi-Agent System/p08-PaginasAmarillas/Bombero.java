import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class Bombero extends Agent {

    protected void setup() {
        // Descripción del servicio
        ServiceDescription servicio = new ServiceDescription();
        // Su servicio es apagar fuego
        servicio.setType("apaga fuego");
        servicio.setName(this.getLocalName());
        
        registrarServicio(servicio);
        recibirMensajes("fuego", "Voy a apagar el incendio");
    }

    // Método para registrar un servicio
    protected void registrarServicio(ServiceDescription sd) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    // Método para añadir un comportamiento que recibe mensajes
    protected void recibirMensajes(final String mensaje, final String respuesta) {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getContent().equalsIgnoreCase(mensaje)) {
                        ACLMessage reply = msg.createReply();
                        reply.setContent(respuesta);
                        myAgent.send(reply);
                    } else {
                        block(); // suspende el comportamiento si no hay mensajes relevantes
                    }
                } else {
                    block(); // suspende hasta que llegue un nuevo mensaje
                }
            }
        });
    }
}
