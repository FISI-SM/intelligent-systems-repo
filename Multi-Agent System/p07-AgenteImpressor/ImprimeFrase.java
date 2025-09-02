import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class ImprimeFrase extends Behaviour {
    int numExecucao = 1;
    long delay;
    long tempoInicial = System.currentTimeMillis();
    
    public ImprimeFrase(Agent a, long delay) {
        super(a);
        this.delay = delay;
    }

    public void action() {
        block(delay); // pausa la ejecución del comportamiento por el tiempo indicado (en milisegundos)
        System.out.println("& Tiempo " + (System.currentTimeMillis() - tempoInicial) 
                           + ": Mi nombre es " + myAgent.getLocalName());
        numExecucao = numExecucao + 1;
    }

    public boolean done() {
        // el comportamiento terminará cuando se haya ejecutado más de 10 veces
        return numExecucao > 10;
    }

    public int onEnd() {
        System.out.println(myAgent.getLocalName() + ": ¡Mi comportamiento fue finalizado! ¡Hasta luego...");
        return 0;
    }
}
