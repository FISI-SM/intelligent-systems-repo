/**
 * Para compilar este código:
 * 
 * Compilar y ejecutar en Linux:
 * $ javac -d out -cp "../jade.jar" *.java
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "mi:MiAgente"
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "mi:MiAgente"   # sin GUI
 * $ java -cp ".:../jade.jar:out" jade.Boot -gui -agents "mi:MiAgente"   # con GUI
 * 
 * Compilar en Windows:
 * $ javac -d out -cp "..\\jade.jar" *.java 
 * $ java -cp ".;..\\jade.jar;out" jade.Boot -agents "mi:MiAgente"
 **/

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
