import jade.util.leap.Serializable;

/**
 * Una clase cuyos objetos serán serializados debe
 * implementar la interfaz Serializable de JADE.
 */
public class Musicos implements Serializable {
    String nome;
    int idade;
    String banda;

    // Constructor
    public Musicos(String nome, int idade, String banda) {
        this.nome = nome;
        this.idade = idade;
        this.banda = banda;
    }

    // Método para imprimir la información del músico
    public void Imprimir() {
        System.out.println("-------------------------");
        System.out.println("Nombre: " + nome);
        System.out.println("Edad:   " + idade);
        System.out.println("Banda:  " + banda);
        System.out.println("-------------------------\n");
    }
}
