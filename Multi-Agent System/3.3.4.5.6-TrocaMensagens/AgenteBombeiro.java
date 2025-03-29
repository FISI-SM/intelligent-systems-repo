/**
 * java jade.Boot -gui -agents "Bombeiro:AgenteBombeiro;A1:AgenteAlarmado"
 * */
import jade.core.Agent ;
import jade.core.behaviours.CyclicBehaviour ;
import jade.lang.acl.ACLMessage ;

public class AgenteBombeiro extends Agent{
	protected void setup() {
		addBehaviour (new CyclicBehaviour ( this ) {
			public void action() {

				ACLMessage msg = myAgent.receive( ) ;
				if ( msg != null ) {
					String content = msg.getContent();//com equals Ignore case fazemos uma compara¸ccao
						// n˜ao case −s e n s i t i v e .
					if( content.equalsIgnoreCase( "Fogo") ) {
						System.out.println( "O agente " + msg.getSender().getName() + " avisou de um incendio ");
						System.out.println( "Vou ativar os procedimentos de combate ao incendio !");
					}
				} else
					block();
			} // fim do action()
		}); // fim do add Behaviour()
	}
}

