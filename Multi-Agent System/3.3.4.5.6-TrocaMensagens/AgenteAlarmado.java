import jade.core.Agent ;
import jade.core.behaviours.OneShotBehaviour ;
import jade.lang.acl.ACLMessage ;
import jade.core.AID;

public class AgenteAlarmado extends Agent{
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
	}
}	
