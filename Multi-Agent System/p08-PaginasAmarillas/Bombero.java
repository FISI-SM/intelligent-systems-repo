/*
 * Archivo:      Bombero.java
 *
 * Propósito:    Mostrar un agente proveedor que registra el servicio "apaga
 *               fuego" en las Páginas Amarillas (DF) y responde "Voy a apagar
 *               el incendio" cuando recibe el mensaje "fuego".
 *
 * Compilación:  No se ejecuta solo. Se compila junto con Solicitante.java
 *               (javac ... *.java). Vea los comandos en Solicitante.java.
 *               Se inicia junto con el Solicitante en el mismo -agents.
 *
 * Entrada:      Mensajes ACL de otros agentes; reacciona solo al contenido
 *               "fuego".
 *
 * Salida:       No imprime nada; envía la respuesta "Voy a apagar el
 *               incendio" al remitente.
 *
 * Notas:
 *    1. DFService.register publica el servicio; el Solicitante lo encuentra
 *       con DFService.search.
 *    2. Bombero, Medico y Policia solo cambian el tipo de servicio, el pedido
 *       y la respuesta.
 *    3. Si deja la plataforma activa, el registro sigue en el DF hasta que el
 *       agente termina.
 */

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
