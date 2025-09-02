package pe.edu.fisi.AgentProject;

import jade.core.Agent;

  public class HelloAgent extends Agent
  {
      @Override
	protected void setup()
      {
          System.out.println("Hello World From Maven Project. ");
          System.out.println("My name is "+ getLocalName());
      }
  }
