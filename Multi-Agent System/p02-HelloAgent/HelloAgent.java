/**
 * To compile this code:
 * Commpile and run in Linux
 * $ javac -cp .:../jade.jar HelloAgent.java
 * $ java -cp .:../jade.jar jade.Boot -agents "hello:HelloAgent" #without GUI
 * $ java -cp .:../jade.jar jade.Boot -gui -agents "hello:HelloAgent" #with GUI
 * Compile in Windows
 * $ javac -cp .;../jade.jar HelloAgent.java 
 * $ 
 **/

 import jade.core.Agent;

  public class HelloAgent extends Agent 
  { 
      protected void setup() 
      { 
          System.out.println("Hello World. ");
          System.out.println("My name is "+ getLocalName()); 
      }
  }
