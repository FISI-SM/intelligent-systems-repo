/**
 * Ejemplo de ejecución:
 * $ java -cp "jade.jar:classes:." jade.Boot -gui -agents "Contador:AgenteContador"
 */

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteContador extends Agent {

    protected void setup() {

        System.out.println("Agente Contador inicializado.\n" + "Esperando información...");

        addBehaviour(new CyclicBehaviour(this) { // inicio del comportamiento
            Musicos[] musicos = new Musicos[5]; // vector de la clase Musicos
            int cont = 0;

            public void action() {
                ACLMessage msg = receive(); // captura nueva mensaje

                if (msg != null) { // si existe mensaje
                    try { // extrae el objeto
                        musicos[cont] = (Musicos) msg.getContentObject();
                        // imprime la información del objeto
                        musicos[cont].Imprimir();
                        cont = cont + 1;
                    } catch (Exception e) {
                        cont = 0;
                    }
                } else {
                    block(); // espera un nuevo mensaje
                }
            }
        }); // fin del comportamiento
    } // fin del método setup() del agente
}
