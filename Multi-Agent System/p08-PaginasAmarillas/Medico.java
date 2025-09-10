import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class Medico extends Agent {

    protected void setup() {
        // Descripción del servicio
        ServiceDescription servicio = new ServiceDescription();
        // Su servicio es salvar vidas
        servicio.setType("salva vidas");
        servicio.setName(this.getLocalName());

        registrarServicio(servicio);
        recibirMensajes("enfermo", "Voy a salvar al enfermo");
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

    // Método para añadir un comportamiento para recibir mensajes
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
                        block(); // suspende si el mensaje no coincide
                    }
                } else {
                    block(); // suspende si no hay mensajes
                }
            }
        });
    }
}
