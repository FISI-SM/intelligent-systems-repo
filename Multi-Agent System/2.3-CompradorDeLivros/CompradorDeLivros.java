/**
 * java -cp "jade.jar:classes:." jade.Boot -name plataforma-de-teste Jose:CompradorDeLivros("O-pequenho-Principe")
 * 
 * */
import jade.core.Agent;
import jade.core.AID;

public class CompradorDeLivros extends Agent{
	private String livrosComprar;
	protected void setup(){
		// imprime mensagem de Bem−Vindo
		System.out.println( "Ola !!! Eu sou o Agente comprador "+ getLocalName() + " e estou pronto para comprar!");

		// captura o tı t u l o do l i v r o que comprara , que f o i passado como argumento de i n i c i a l i z aca o
		Object[] args = getArguments();
		if( args != null && args.length >0){	
			livrosComprar = ( String ) args[0];
			System.out.println( " Pretendo comprar o livro: "+ livrosComprar );
		} else	{	
			// f i n a l i z a o agente
			System.out.println( "Nao tenho livros para comprar!"); 
			doDelete(); // invoca a execuca o do metodo takeDown()
		}
	}

	protected void takeDown() {
			System.out.println( "Agente comprador" + getAID().getName() + " esta finalizado");
	}
}