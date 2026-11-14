/*
 * Archivo:      ParamAgent.java
 *
 * Propósito:    Mostrar cómo un agente JADE recibe argumentos de
 *               inicialización con getArguments(): imprime cada argumento y
 *               el cuadrado del primero.
 *
 * Compilación (desde la carpeta p02-HelloAgent; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "fred:ParamAgent(3,Allo there)"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "fred:ParamAgent(3,Allo there)"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "fred:ParamAgent(3,Allo there)"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Dos argumentos entre paréntesis: un entero y un texto.
 *
 * Salida:       "p0: 3", "p1: Allo there" e "i*i= 9".
 *
 * Notas:
 *    1. La lista de agentes (-agents) va entre comillas dobles para que bash,
 *       zsh, CMD y PowerShell la reciban igual. Los argumentos de un agente
 *       van entre paréntesis y separados por comas, sin comillas internas:
 *       JADE conserva las comillas como parte del valor.
 *    2. Si se escribe ParamAgent(3,"Allo there"), el agente recibe el texto
 *       con las comillas incluidas.
 *    3. El primer argumento debe ser un entero; si no, Integer.parseInt lanza
 *       NumberFormatException.
 *    4. Es la versión agente de Parameters.java (p01).
 *    5. La plataforma JADE sigue activa después del agente; deténgala con
 *       Ctrl+C.
 *    6. El separador del classpath es ':' en Linux/macOS y ';' en Windows. En
 *       PowerShell el classpath va entre comillas, porque ';' separa comandos.
 *    7. -encoding UTF-8 evita errores con tildes y ñ al compilar en Windows
 *       con JDK 17 o anterior.
 *    8. En Mac M1 use un JDK nativo arm64 (por ejemplo, Temurin aarch64). JADE
 *       es Java puro y no necesita nada especial. Probado con JDK 17 y JADE
 *       4.5.0.
 */

 import jade.core.Agent;

 public class ParamAgent extends Agent 
 { 
	protected void setup() 
	{ 
		Object[] args = getArguments();
		String s;
		if (args != null) {
			for (int i = 0; i<args.length; i++) {
				s = (String) args[i];
				System.out.println("p" + i + ": " + s);
			}
			
			int i = Integer.parseInt( (String) args[0] );
			s     = (String) args[1];
			
			System.out.println("i*i= " + i*i);			
		}
	}
 }
