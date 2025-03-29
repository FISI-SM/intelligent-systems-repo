import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class Bombeiro extends Agent{

    protected void setup() {
      // Descrição do Serviço
      ServiceDescription servico = new ServiceDescription();
      //Seu serviço é salvar vidas
      servico.setType("apaga fogo");
      servico.setName(this.getLocalName());
      registraServico(servico);
      RecebeMensagens("fogo", "Vou apagar o incêndio");
    }

    //método para registrar servico
    protected void registraServico(ServiceDescription sd) {
      DFAgentDescription dfd = new DFAgentDescription();
      dfd.addServices(sd);
      try {
        DFService.register(this, dfd);
      } catch (FIPAException e) {
        e.printStackTrace();
      }
    }

    //Método para adicionar wn comportamento para receber mensagens
    protected void RecebeMensagens(final String mensagem, final String resp){
      addBehaviour(new CyclicBehaviour(this) {
          public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
              if (msg.getContent().equalsIgnoreCase(mensagem)) {
                ACLMessage reply = msg.createReply();
                reply.setContent(resp);
                myAgent.send(reply);
              } else
                block();
            }            
          }
        });
    }
}
        
          