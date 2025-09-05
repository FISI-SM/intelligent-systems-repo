/**
 * ------------------------------------------------------------
 *   CompradorDeLibros: Ejemplo de agente que recibe un libro
 *                      como argumento en su inicialización
 *
 *   Compilar en Windows:
 *   $ javac -cp .;../jade.jar CompradorDeLibros.java
 *
 *   Compilar en Linux/Mac:
 *   $ javac -cp .:../jade.jar CompradorDeLibros.java
 *
 *   Ejecutar en Linux/Mac:
 *   $ java -cp .:../jade.jar jade.Boot -name plataforma-de-prueba Jose:CompradorDeLibros("El-principito")
 *
 *   Ejecutar en Windows (CMD o PowerShell):
 *   $ java -cp .;../jade.jar jade.Boot -name plataforma-de-prueba Jose:CompradorDeLibros("El-principito")
 *
 *   Nota:
 *   - En Linux/Mac el separador de classpath es ":".
 *   - En Windows el separador de classpath es ";".
 * ------------------------------------------------------------
 */

import jade.core.Agent;
import jade.core.AID;

public class CompradorDeLibros extends Agent {
    private String librosComprar;

    protected void setup() {
        // imprime mensaje de bienvenida
        System.out.println("¡Hola! Soy el agente comprador " + getLocalName() + " y estoy listo para comprar!");

        // captura el título del libro que comprará, que fue pasado como argumento de inicialización
        Object[] args = getArguments();
        if (args != null && args.length > 0) {    
            librosComprar = (String) args[0];
            System.out.println("Pretendo comprar el libro: " + librosComprar);
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
