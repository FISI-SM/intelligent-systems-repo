/** ---------------------------------------------
   Parameters:   An Agent receiving parameters             

   Usage:    % javac Parameters.java
             % java Parameters a b cd 123 "Hola Mundo"
*/

  public class Parameters {

	public static void main(String args[]){ 		
		String s;
		if (args != null) {
			for (int i = 0; i<args.length; i++) {
				s = args[i];
				System.out.println("p" + i + ": " + s);
			}
		}
	}
 }
