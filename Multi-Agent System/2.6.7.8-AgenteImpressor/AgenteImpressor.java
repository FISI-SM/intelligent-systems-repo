import jade.core.Agent;


public class AgenteImpressor  extends Agent {

	protected void setup(){
		System.out.println( "Ola! Eu sou um agente impressor!"); 
		System.out.println( "# Vou executar meu comportamento"); 
		addBehaviour(new ImprimeFrase( this , 5000 ) );
	}
}
