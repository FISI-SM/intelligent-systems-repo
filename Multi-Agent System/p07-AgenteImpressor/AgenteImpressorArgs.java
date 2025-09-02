import jade.core.Agent;
import jade.core.*;

public class AgenteImpressorArgs extends Agent {
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            long valor = Long.parseLong((String) args[0]);
            System.out.println("¡Hola! Soy un agente impresor!"); 
            System.out.println("# Voy a ejecutar mi comportamiento");
            addBehaviour(new ImprimeFrase(this, valor));
        } else {
            System.out.println("No pasaste argumentos");
        }
    }
}
