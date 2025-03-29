import jade.core.Agent ;
import jade.core.behaviours.OneShotBehaviour ;
import jade.core.behaviours.CyclicBehaviour ;
import jade.lang.acl.ACLMessage ;
import jade.core.AID;

public class AgenteAlarmado2 extends Agent{
	protected void setup() {
		addBehaviour(new OneShotBehaviour( this ) {
			public void action() {
				ACLMessage msg = new ACLMessage( ACLMessage.INFORM); 
				msg.addReceiver(new AID("Bombeiro" ,AID.ISLOCALNAME) );
				msg.setLanguage( " Portugues" );
				msg.setOntology( " Emergencia "); 
				msg.setContent( "Fogo");
				
				myAgent.send( msg);
			}
		});

		addBehaviour(new CyclicBehaviour( this ) {
			public void action(){
				ACLMessage msg = myAgent.receive();
				if( msg != null ) {
					String content = msg.getContent();
					System.out.println( "--> " + msg.getSender().getName() + ": " + content );
				}else
					//com o block() bloqueamos o comportamento ate que uma nova
					// mensagem chegue ao agente e assim evitamos consumir c i c l o s
					// da cPU.
					block();
			}
		});			
	}
}	