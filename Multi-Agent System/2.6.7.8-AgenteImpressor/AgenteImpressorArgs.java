import jade.core.Agent;
import jade.core.*;

public class AgenteImpressorArgs extends Agent {
	protected void setup() {
		Object[] args = getArguments();
		if(args != null && args.length >0){
			long valor	= Long.parseLong(( String ) args[ 0 ] );
			System.out.println( "Ola! Eu sou um agente impressor!"); 
			System.out.println( "# Vou executar meu comportamento");
			addBehaviour(new ImprimeFrase( this , valor ) );
		} else{
			System.out.println( "Voce nao passou argumentos");
		}
	}
}
