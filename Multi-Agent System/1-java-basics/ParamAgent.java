// ------------------------------------------------------------
//   ParamAgent:   An Agent receiving parameters             
//
//   Usage:    % javac ParamAgent.java
//             % java jade.Boot  fred:ParamAgent(3 "Allo there")
//
// ... on UNIX, the agent specifier and arguments must be quoted:
//
//             % java jade.Boot 'fred:ParamAgent(3 "Allo there")'
// ------------------------------------------------------------

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
			System.exit(1);
		}
	}
 }
