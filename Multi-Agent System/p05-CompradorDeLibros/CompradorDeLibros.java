/*
 * Archivo:      CompradorDeLibros.java
 *
 * Propósito:    Mostrar un agente que recibe el título de un libro como
 *               argumento de inicialización y que se elimina a sí mismo
 *               (doDelete) si no lo recibe.
 *
 * Compilación (desde la carpeta p05-CompradorDeLibros; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "Jose:CompradorDeLibros(El principito)"
 *       java -cp .:../jade.jar jade.Boot -agents "Jose:CompradorDeLibros"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "Jose:CompradorDeLibros(El principito)"
 *       java -cp .;..\jade.jar jade.Boot -agents "Jose:CompradorDeLibros"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "Jose:CompradorDeLibros(El principito)"
 *       java -cp ".;..\jade.jar" jade.Boot -agents "Jose:CompradorDeLibros"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *    Con argumento: el agente sigue vivo.
 *    Sin argumento: el agente termina y ejecuta takeDown().
 *
 * Entrada:      Opcional: el título del libro entre paréntesis.
 *
 * Salida:       Con argumento: el saludo y "Pretendo comprar el libro: El
 *               principito". Sin argumento: el saludo, "¡No tengo libros para
 *               comprar!" y "El agente comprador Jose@<host>:1099/JADE ha
 *               finalizado."
 *
 * Notas:
 *    1. doDelete() no detiene el agente al instante: JADE termina setup() y
 *       luego llama a takeDown().
 *    2. El título puede tener espacios; no use comillas dentro de los
 *       paréntesis.
 *    3. La plataforma JADE sigue activa después del agente; deténgala con
 *       Ctrl+C.
 *    4. El separador del classpath es ':' en Linux/macOS y ';' en Windows. En
 *       PowerShell el classpath va entre comillas, porque ';' separa comandos.
 *    5. -encoding UTF-8 evita errores con tildes y ñ al compilar en Windows
 *       con JDK 17 o anterior.
 *    6. En Mac M1 use un JDK nativo arm64 (por ejemplo, Temurin aarch64). JADE
 *       es Java puro y no necesita nada especial. Probado con JDK 17 y JADE
 *       4.5.0.
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
