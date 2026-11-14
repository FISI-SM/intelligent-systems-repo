/*
 * Archivo:      MiComportamiento.java
 *
 * Propósito:    Definir un comportamiento genérico (Behaviour) que imprime un
 *               saludo con el nombre de su agente y termina después de 11
 *               ejecuciones.
 *
 * Compilación:  No se ejecuta solo. Se compila junto con MiAgente.java
 *               (javac ... *.java). Vea los comandos en MiAgente.java.
 *
 * Entrada:      El agente dueño, recibido en el constructor.
 *
 * Salida:       Una línea "* ¡Hola Mundo! ... Mi nombre es <agente>" por cada
 *               ejecución de action().
 *
 * Notas:
 *    1. action() contiene el trabajo de un paso; done() decide si el
 *       comportamiento termina.
 *    2. done() devuelve true cuando i > 10, así que action() se ejecuta 11
 *       veces.
 *    3. myAgent es el agente que agregó este comportamiento.
 */

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class MiComportamiento extends Behaviour {
    int i = 0;

    public MiComportamiento(Agent a) {
        super(a);
    }

    public void action() {
        System.out.println("* ¡Hola Mundo! ... Mi nombre es " + myAgent.getLocalName());
        i = i + 1;
    }

    public boolean done() {
        // si este método retorna TRUE, el comportamiento será finalizado
        return i > 10;
    }
}
