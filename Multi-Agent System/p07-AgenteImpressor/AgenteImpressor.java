/**
 * Para compilar este código:
 * 
 * Compilar y ejecutar en Linux:
 * $ javac -d out -cp "../jade.jar" *.java
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "agentA:AgenteImpressor"
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "agentA:AgenteImpressor"   # sin GUI
 * $ java -cp ".:../jade.jar:out" jade.Boot -gui -agents "agentA:AgenteImpressor"   # con GUI
 * 
 * Compilar en Windows:
 * $ javac -d out -cp "..\\jade.jar" *.java 
 * $ java -cp ".;..\\jade.jar;out" jade.Boot -agents "agentA:AgenteImpressor"
 **/

import jade.core.Agent;

public class AgenteImpressor extends Agent {

    protected void setup() {
        System.out.println("¡Hola! Soy un agente impresor!"); 
        System.out.println("# Voy a ejecutar mi comportamiento"); 
        addBehaviour(new ImprimeFrase(this, 5000));
    }
}
