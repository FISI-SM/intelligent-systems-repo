/**
 * To compile this code:
 * Commpile and run in Linux
 * $ javac -d out -cp "../jade.jar" *.java
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "agentA:AgenteImpressor"
 * $ java -cp ".:../jade.jar:out" jade.Boot -agents "agentA:AgenteImpressor" #without GUI
 * $ java -cp ".:../jade.jar:out" jade.Boot -gui -agents "agentA:AgenteImpressor" #with GUI
 * Compile in Windows
 * $ javac -d out -cp "..\jade.jar" *.java 
 * $ java -cp ".;..\jade.jar;out" jade.Boot -agents "agentA:AgenteImpressor"
 **/

import jade.core.Agent;

public class AgenteImpressor  extends Agent {

	protected void setup(){
		System.out.println( "Ola! Eu sou um agente impressor!"); 
		System.out.println( "# Vou executar meu comportamento"); 
		addBehaviour(new ImprimeFrase( this , 5000 ) );
	}
}
