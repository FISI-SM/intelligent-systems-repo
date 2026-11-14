/*
 * Archivo:      HelloAgent.java
 *
 * Propósito:    Mostrar el agente JADE mínimo: una clase que extiende Agent y
 *               sobrescribe setup(), el método que JADE ejecuta al crear el
 *               agente.
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
 *       java -cp .:../jade.jar jade.Boot -agents "hello:HelloAgent"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "hello:HelloAgent"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "hello:HelloAgent"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Ninguna.
 *
 * Salida:       "Hello World." y "My name is hello" (hello es el nombre local
 *               dado en -agents), después de los mensajes de inicio de JADE.
 *
 * Notas:
 *    1. En -agents "hello:HelloAgent", hello es el nombre del agente y
 *       HelloAgent es su clase.
 *    2. Un agente no tiene main: lo crea la plataforma (jade.Boot), que llama
 *       a setup().
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

  public class HelloAgent extends Agent 
  { 
      protected void setup() 
      { 
          System.out.println("Hello World. ");
          System.out.println("My name is "+ getLocalName()); 
      }
  }
