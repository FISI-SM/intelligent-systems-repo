/**
 * java jade.Boot -gui -agents "Bombeiro:AgenteBombeiro;A1:AgenteAlarmado"
 * */
import jade.core.Agent ;
import jade.core.behaviours.CyclicBehaviour ;
import jade.lang.acl.ACLMessage ;

public class AgenteBombeiro2 extends Agent{
	protected void setup(){
		addBehaviour (new CyclicBehaviour( this ) {
			public void action() {
				ACLMessage msg = myAgent.receive();
				if( msg != null ) {
					ACLMessage reply = msg.createReply();
					String content = msg.getContent();
					if( content.equalsIgnoreCase( "Fogo") ) {
						reply.setPerformative( ACLMessage.INFORM);
						reply.setContent("Recebi seu aviso! Obrigado por auxiliar meu servico ");
						myAgent.send( reply );
						System.out.println( "O agente "+ msg.getSender().getName() +" avisou de um incendio ");
						System.out.println( "Vou ativar os procedimentos de combate ao incendio !");
					}
				}else
					block();
			}
		});
	}
}
