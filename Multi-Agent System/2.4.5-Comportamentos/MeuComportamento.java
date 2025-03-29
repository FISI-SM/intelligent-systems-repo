import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class MeuComportamento extends Behaviour {
  	int i =0;
	public MeuComportamento( Agent a ) {
		super( a );
	}

	public void action(){
		System.out.println("* Ola Mundo! ... Meu nome e " + myAgent.getLocalName());
		i=i +1;
	}
	
	public boolean done() {
		// caso e s t e metodo retorne TRUE o comportamento ser a f i n a l i z a d o
		return i >10;
	}
}

