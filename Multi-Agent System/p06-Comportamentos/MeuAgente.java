/**
 * Para compilar este código:
 * 
 * Compilar y ejecutar en Linux:
 * $ javac -d out -cp "../jade.jar" *.java
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "meu:MeuAgente"
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "meu:MeuAgente"   # sin GUI
 * $ java -cp ".:../jade.jar:out" jade.Boot -gui -agents "meu:MeuAgente"   # con GUI
 * 
 * Compilar en Windows:
 * $ javac -d out -cp "..\\jade.jar" *.java 
 * $ java -cp ".;..\\jade.jar;out" jade.Boot -agents "meu:MeuAgente"
 **/

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class MeuAgente extends Agent {
    protected void setup() {
        System.out.println("¡Hola! Soy un agente.");
        System.out.println("Estoy iniciando mi comportamiento...");
        addBehaviour(new MeuComportamento(this));
    }
}
