/*
 * Archivo:      AgenteInventario.java
 *
 * Propósito:    Mostrar cómo un agente envía objetos Java serializables en
 *               mensajes ACL con setContentObject(): envía 5 objetos Musicos
 *               al agente Contador y termina.
 *
 * Compilación (desde la carpeta p10-EnvioObjetos; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "Contador:AgenteContador;Inventario:AgenteInventario"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "Contador:AgenteContador;Inventario:AgenteInventario"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "Contador:AgenteContador;Inventario:AgenteInventario"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Ninguna.
 *
 * Salida:       "Toda la información fue enviada" (en takeDown()). Los datos
 *               los imprime AgenteContador.
 *
 * Notas:
 *    1. Basado en el Código 3.8 del material original.
 *    2. done() devuelve true después de 5 envíos y llama a doDelete().
 *    3. La plataforma JADE sigue activa después del agente; deténgala con
 *       Ctrl+C.
 *    4. El separador del classpath es ':' en Linux/macOS y ';' en Windows. En
 *       PowerShell el classpath va entre comillas, porque ';' separa comandos.
 *    5. -encoding UTF-8 evita errores con tildes y ñ al compilar en Windows
 *       con JDK 17 o anterior.
 *    6. En Mac M1 use un JDK nativo arm64 (por ejemplo, Temurin aarch64). JADE
 *       es Java puro y no necesita nada especial. Probado con JDK 17 y JADE
 *       4.5.0.
 */

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import java.io.IOException;

public class AgenteInventario extends Agent {
    Musicos[] mus = new Musicos[5];

    protected void setup() {
        // Inicializamos el arreglo de músicos
        mus[0] = new Musicos("Claudia Leite", 30, "Babado Novo");
        mus[1] = new Musicos("Paula Toller", 45, "Kid Abelha");
        mus[2] = new Musicos("Rogerio Flausino", 37, "Jota Quest");
        mus[3] = new Musicos("Laura Pausini", 33, null);
        mus[4] = new Musicos("Bono Vox", 47, "U2");

        addBehaviour(new SimpleBehaviour(this) { // inicio del comportamiento

            int cont = 0;

            public void action() {
                try {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID("Contador", AID.ISLOCALNAME));
                    msg.setContentObject(mus[cont]);
                    myAgent.send(msg); // envía el mensaje con el objeto Musicos

                    cont = cont + 1;
                    // block(100); // se podría usar para espaciar los envíos
                } catch (IOException ex) {
                    System.out.println("Error en el envío del mensaje");
                }
            }

            public boolean done() {
                if (cont > 4) {
                    myAgent.doDelete(); // finaliza el agente
                    return true;
                } else {
                    return false;
                }
            }
        }); // fin del comportamiento
    } // fin del método setup() del agente

    // La invocación del método doDelete() dispara la ejecución de takeDown()
    protected void takeDown() {
        System.out.println("Toda la información fue enviada");
    }
}
