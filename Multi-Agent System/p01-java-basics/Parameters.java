/*
 * Archivo:      Parameters.java
 *
 * Propósito:    Mostrar cómo un programa Java recibe argumentos de línea de
 *               comandos en el arreglo args de main y los imprime con su
 *               posición.
 *
 * Compilación (desde la carpeta p01-java-basics):
 *    Linux / macOS (Intel y Apple Silicon M1), Windows CMD y PowerShell:
 *       javac -encoding UTF-8 Parameters.java
 *
 * Ejecución (igual en todos los sistemas):
 *       java Parameters a b cd 123 "Hola Mundo"
 *
 * Entrada:      Los argumentos escritos después del nombre de la clase.
 *
 * Salida:       Una línea por argumento: "p0: a", "p1: b", "p2: cd", "p3:
 *               123", "p4: Hola Mundo".
 *
 * Notas:
 *    1. Las comillas agrupan "Hola Mundo" en un solo argumento; el shell las
 *       quita antes de llamar a Java.
 *    2. Todos los argumentos llegan como String; para usarlos como números hay
 *       que convertirlos (por ejemplo, Integer.parseInt).
 *    3. Es la versión Java del agente ParamAgent (p02), que recibe sus
 *       argumentos con getArguments().
 */

  public class Parameters {

	public static void main(String args[]){ 		
		String s;
		if (args != null) {
			for (int i = 0; i<args.length; i++) {
				s = args[i];
				System.out.println("p" + i + ": " + s);
			}
		}
	}
 }
