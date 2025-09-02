import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class MeuComportamento extends Behaviour {
    int i = 0;

    public MeuComportamento(Agent a) {
        super(a);
    }

    public void action() {
        System.out.println("* ¡Hola Mundo! ... Mi nombre es " + myAgent.getLocalName());
        i = i + 1;
    }

    public boolean done() {
        // si este método retorna TRUE, el comportamiento será finalizado
        return i > 10;
    }
}
