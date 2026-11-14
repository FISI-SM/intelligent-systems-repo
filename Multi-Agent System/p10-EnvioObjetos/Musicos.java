/*
 * Archivo:      Musicos.java
 *
 * Propósito:    Definir el objeto que viaja dentro de los mensajes: un músico
 *               con nombre, edad y banda, y un método para imprimirlo.
 *
 * Compilación:  No se ejecuta solo. Se compila junto con AgenteEstoque.java y AgenteContador.java
 *               (javac ... *.java). Vea los comandos en AgenteEstoque.java y AgenteContador.java.
 *
 * Entrada:      Nombre, edad y banda (constructor).
 *
 * Salida:       Imprimir() muestra los tres datos entre líneas de guiones.
 *
 * Notas:
 *    1. Implementa jade.util.leap.Serializable para poder enviarse con
 *       setContentObject().
 *    2. Los atributos usan nombres en portugués (nome, idade) del material
 *       original.
 */

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
