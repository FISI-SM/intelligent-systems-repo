/**
 * To compile this code:
 * Commpile and run in Linux
 * $ javac -d out -cp "../jade.jar" *.java
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "meu:MeuAgente"
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "meu:MeuAgente" #without GUI
 * $ java -cp ".:../jade.jar:out" jade.Boot -gui -agents "meu:MeuAgente" #with GUI
 * Compile in Windows
 * $ javac -d out -cp "..\jade.jar" *.java 
 * $ java -cp ".;..\jade.jar;out" jade.Boot -agents "meu:MeuAgente"
 **/

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
public class  MeuAgente extends Agent{
    protected void setup() {
        System.out.println("Ola , eu sou um agente.");
        System.out.println("Estou disparando meu comportamento ...");
        addBehaviour(new MeuComportamento(this));

    }
}