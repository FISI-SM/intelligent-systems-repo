/*
 * Archivo:      ImprimeFrase.java
 *
 * Propósito:    Definir un comportamiento que imprime el tiempo transcurrido
 *               y el nombre de su agente, con una pausa entre impresiones, y
 *               termina después de 10 ejecuciones.
 *
 * Compilación:  No se ejecuta solo. Se compila junto con AgenteImpressor.java o AgenteImpressorArgs.java
 *               (javac ... *.java). Vea los comandos en AgenteImpressor.java o AgenteImpressorArgs.java.
 *
 * Entrada:      El agente dueño y la pausa en milisegundos (constructor).
 *
 * Salida:       Líneas "& Tiempo <ms>: Mi nombre es <agente>" y, al terminar,
 *               "<agente>: ¡Mi comportamiento fue finalizado! ¡Hasta
 *               luego...".
 *
 * Notas:
 *    1. block(delay) no detiene el programa: suspende este comportamiento
 *       hasta que pase el tiempo o llegue un mensaje.
 *    2. action() continúa después de block(); por eso la primera línea sale en
 *       ~0 ms y la pausa se nota desde la segunda.
 *    3. onEnd() se ejecuta una vez, cuando done() devuelve true.
 */

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class ImprimeFrase extends Behaviour {
    int numExecucao = 1;
    long delay;
    long tempoInicial = System.currentTimeMillis();
    
    public ImprimeFrase(Agent a, long delay) {
        super(a);
        this.delay = delay;
    }

    public void action() {
        block(delay); // pausa la ejecución del comportamiento por el tiempo indicado (en milisegundos)
        System.out.println("& Tiempo " + (System.currentTimeMillis() - tempoInicial) 
                           + ": Mi nombre es " + myAgent.getLocalName());
        numExecucao = numExecucao + 1;
    }

    public boolean done() {
        // el comportamiento terminará cuando se haya ejecutado más de 10 veces
        return numExecucao > 10;
    }

    public int onEnd() {
        System.out.println(myAgent.getLocalName() + ": ¡Mi comportamiento fue finalizado! ¡Hasta luego...");
        return 0;
    }
}
