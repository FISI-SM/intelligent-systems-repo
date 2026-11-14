/*
 * Archivo:      HelloWorld.java
 *
 * Propósito:    Mostrar el programa Java mínimo: una clase pública con el
 *               método main, que imprime un saludo en la consola. Sirve para
 *               comprobar que el JDK está instalado antes de usar JADE.
 *
 * Compilación (desde la carpeta p01-java-basics):
 *    Linux / macOS (Intel y Apple Silicon M1), Windows CMD y PowerShell:
 *       javac -encoding UTF-8 HelloWorld.java
 *
 * Ejecución (igual en todos los sistemas):
 *       java HelloWorld
 *
 * Entrada:      Ninguna.
 *
 * Salida:       El texto "Hello, World".
 *
 * Notas:
 *    1. El nombre del archivo debe coincidir con el nombre de la clase
 *       pública.
 *    2. javac genera HelloWorld.class; java recibe el nombre de la clase, sin
 *       la extensión .class.
 *    3. Compruebe la instalación con: java -version y javac -version.
 *    4. En Mac M1 use un JDK nativo arm64 (por ejemplo, Temurin aarch64). JADE
 *       es Java puro y no necesita nada especial. Probado con JDK 17 y JADE
 *       4.5.0.
 */

public class HelloWorld {

    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        System.out.println("Hello, World");
    }

}
