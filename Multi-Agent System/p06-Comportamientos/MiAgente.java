/*
 * Archivo:      MiAgente.java
 *
 * Propósito:    Mostrar cómo un agente usa un comportamiento propio: en
 *               setup() agrega un MiComportamiento (definido en
 *               MiComportamiento.java).
 *
 * Compilación (desde la carpeta p06-Comportamientos; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "mi:MiAgente"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "mi:MiAgente"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "mi:MiAgente"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Ninguna.
 *
 * Salida:       "¡Hola! Soy un agente.", "Estoy iniciando mi
 *               comportamiento..." y 11 líneas "* ¡Hola Mundo! ... Mi nombre
 *               es mi".
 *
 * Notas:
 *    1. Compile con *.java: javac necesita MiComportamiento.java para compilar
 *       MiAgente.java.
 *    2. JADE ejecuta action() y luego pregunta done(); repite hasta que done()
 *       devuelve true.
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

/**
 * ------------------------------------------------------------
 *   MiAgente: Ejemplo básico de agente con comportamiento propio
 *
 *   Compilar en Windows:
 *   $ javac -d out -cp .;../jade.jar *.java
 *
 *   Compilar en Linux/Mac:
 *   $ javac -d out -cp .:../jade.jar *.java
 *
 *   Ejecutar en Linux/Mac:
 *   $ java -cp .:../jade.jar:out jade.Boot -agents mi:MiAgente       # sin GUI
 *   $ java -cp .:../jade.jar:out jade.Boot -gui -agents mi:MiAgente  # con GUI
 *
 *   Ejecutar en Windows (CMD o PowerShell):
 *   $ java -cp .;../jade.jar;out jade.Boot -agents mi:MiAgente       
 *   $ java -cp .;../jade.jar;out jade.Boot -gui -agents mi:MiAgente  
 *
 *   Nota:
 *   - En Linux/Mac el separador de classpath es ":".
 *   - En Windows el separador de classpath es ";".
 * ------------------------------------------------------------
 */

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class MiAgente extends Agent {
    protected void setup() {
        System.out.println("¡Hola! Soy un agente.");
        System.out.println("Estoy iniciando mi comportamiento...");
        addBehaviour(new MiComportamiento(this));
    }
}
