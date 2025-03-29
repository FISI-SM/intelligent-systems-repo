import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class ImprimeFrase extends Behaviour {
	int numExecucao = 1;
	long delay;
	long tempoInicial = System.currentTimeMillis();
	
	public ImprimeFrase( Agent a, long delay) {
		super(a) ;
		this.delay = delay;
	}
	public void action() {
		block(delay);
		System.out.println("& Tempo " + (System.currentTimeMillis() - tempoInicial + ": Meu nome é " + myAgent.getLocalName()));

		numExecucao = numExecucao + 1;
	}
	public boolean done() {
		return numExecucao > 10;
	}
	public int onEnd() {
		System.out.println(myAgent.getLocalName() + ": Meu comportamento foi finalizado! Até mais...");
		return 0 ;
	}
}
