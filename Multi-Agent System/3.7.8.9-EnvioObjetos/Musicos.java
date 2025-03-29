import jade.util.leap.Serializable;
/** Uma c l a s s e que t er ´a seus o b j e t o s
* s e r i a l i z a d o s deve
implementar a i n t e r f a c e Serializable **/
public class Musicos implements Serializable {
  String nome;
  int idade;
  String banda;

  public Musicos(String nome, int idade, String banda) {
    this.nome = nome;
    this.idade = idade;
    this.banda = banda;
  }

  public void Imprimir() {
    System.out.println("	");
    System.out.println("Nome...: " + nome);
    System.out.println("Idade...: " + idade);
    System.out.println("Banda...: " + banda);
    System.out.println("	\n");
  }
}
 