/** Código 3.8: AgenteEstoque.java */
import jade.core.Agent ;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage ;
import jade.core.AID ;
import java.io.IOException ;

public class AgenteEstoque extends Agent {
  Musicos[] mus = new Musicos[5];

  protected void setup() {
    mus[0] = new Musicos(" Claudia Leite", 30, "Babado Novo");
    mus[1] = new Musicos("Paula Toller", 45, "Kid Abelha");
    mus[2] = new Musicos(" Rogerio Flausino", 37, "Jota Quest");
    mus[3] = new Musicos("Laura Pausini", 33, null);
    mus[4] = new Musicos("Bono Vox", 47, "U2");

    addBehaviour(new SimpleBehaviour(this) { // inicio do comportamento

        int cont = 0;

        public void action() {
          try {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Contador", AID.ISLOCALNAME));
            msg.setContentObject(mus[cont]);
            myAgent.send(msg); // envia a mensagem

            cont = cont + 1;
            // block( 100 );
          } catch (IOException ex) {
            System.out.println("Erro no envio da mensagem");
          }
        }

        public boolean done() {
          if (cont > 4) {
            myAgent.doDelete(); // finaliza o agente
            return true;
          } else 
            return false;
        }
      }); // fim do comportamento
	} // fim do metodo setup() do agente
	//A invocacao do metodo do Delete() aciona o metodo takeDown()
	protected void takeDown() {
  		System.out.println("Todas as informacoes foram enviadas");
	}

}