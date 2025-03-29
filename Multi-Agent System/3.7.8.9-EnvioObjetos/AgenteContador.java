/**
 * $ java -cp "jade.jar:classes:." jade.Boot -gui -agents "Contador:AgenteContador"
 * */

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteContador extends Agent {

  protected void setup() {

    System.out.println("Agente Contador inicializado.\n " + " Aguardando informacoes ...");

    addBehaviour(new CyclicBehaviour(this) { // inıcio do comportamento
      Musicos[] musicos = new Musicos[5]; // vetor da classe Musicos
      int cont = 0;

      public void action() {
        ACLMessage msg = receive(); // captura nova mensagem

        if (msg != null) { // se existe mensagem
          try { // extrai o objeto
            musicos[cont] = (Musicos) msg.getContentObject();
            // imprime as informacoes do objeto
            musicos[cont].Imprimir();
            cont = cont + 1;
          } catch (Exception e) {
            cont = 0;
          }
        } else
          block(); // aguarda nova mensagem
      }
    }); // te rmino do comportamento
  } // fim do metodo setup() do agente
}