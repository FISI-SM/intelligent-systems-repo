/**
 * Ejecución de este código:
 * java -cp "jade.jar:classes:." jade.Boot -name plataforma-de-prueba Jose:CompradorDeLivros("El-principito")
 * 
 */
import jade.core.Agent;
import jade.core.AID;

public class CompradorDeLivros extends Agent {
    private String livrosComprar;

    protected void setup() {
        // imprime mensaje de bienvenida
        System.out.println("¡Hola! Soy el agente comprador " + getLocalName() + " y estoy listo para comprar!");

        // captura el título del libro que comprará, que fue pasado como argumento de inicialización
        Object[] args = getArguments();
        if (args != null && args.length > 0) {    
            livrosComprar = (String) args[0];
            System.out.println("Pretendo comprar el libro: " + livrosComprar);
        } else {    
            // finaliza el agente
            System.out.println("¡No tengo libros para comprar!"); 
            doDelete(); // invoca la ejecución del método takeDown()
        }
    }

    protected void takeDown() {
        System.out.println("El agente comprador " + getAID().getName() + " ha finalizado.");
    }
}
