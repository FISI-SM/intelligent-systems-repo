/*
 * Archivo:      Solicitante.java
 *
 * Propósito:    Mostrar el uso de las Páginas Amarillas (DF) de JADE: el
 *               agente busca quién ofrece el servicio pedido (fuego, ladron o
 *               enfermo), le envía un mensaje y muestra la respuesta.
 *
 * Compilación (desde la carpeta p08-PaginasAmarillas; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "soli:Solicitante(ladron);bomb:Bombero;pol:Policia;med:Medico"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "soli:Solicitante(ladron);bomb:Bombero;pol:Policia;med:Medico"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "soli:Solicitante(ladron);bomb:Bombero;pol:Policia;med:Medico"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *    Cambie ladron por fuego o enfermo para pedir otro servicio.
 *
 * Entrada:      Un argumento: fuego, ladron o enfermo.
 *
 * Salida:       "Servicio solicitado: ladron" y, unos 5 segundos después, la
 *               respuesta del proveedor: "( agent-identifier :name
 *               pol@<host>:1099/JADE ...) → Voy a atrapar al ladron".
 *
 * Notas:
 *    1. El TickerBehaviour busca en el DF cada 5 segundos; cuando encuentra un
 *       proveedor, envía el pedido y se detiene con stop().
 *    2. Los agentes se separan con ';' dentro de las comillas de -agents.
 *    3. Si un proveedor no está en -agents, el Solicitante sigue buscando cada
 *       5 segundos sin respuesta.
 *    4. La plataforma JADE sigue activa después del agente; deténgala con
 *       Ctrl+C.
 *    5. El separador del classpath es ':' en Linux/macOS y ';' en Windows. En
 *       PowerShell el classpath va entre comillas, porque ';' separa comandos.
 *    6. -encoding UTF-8 evita errores con tildes y ñ al compilar en Windows
 *       con JDK 17 o anterior.
 *    7. En Mac M1 use un JDK nativo arm64 (por ejemplo, Temurin aarch64). JADE
 *       es Java puro y no necesita nada especial. Probado con JDK 17 y JADE
 *       4.5.0.
 */

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class Solicitante extends Agent {
    protected void setup() {
        // Captura argumentos
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            String argumento = (String) args[0];

            System.out.println("Servicio solicitado: " + argumento);
            // Si el argumento es "fuego"
            if (argumento.equalsIgnoreCase("fuego")) {
                ServiceDescription servicio = new ServiceDescription();
                // El servicio es apagar fuego
                servicio.setType("apaga fuego");
                // Busca quién ofrece ese servicio
                buscar(servicio, "fuego");
            }

            // Si el argumento es "ladron"
            if (argumento.equalsIgnoreCase("ladron")) {
                ServiceDescription servicio = new ServiceDescription();
                // El servicio es atrapar ladrones
                servicio.setType("atrapa ladron");
                buscar(servicio, "ladron");
            }

            // Si el argumento es "enfermo"
            if (argumento.equalsIgnoreCase("enfermo")) {
                ServiceDescription servicio = new ServiceDescription();
                // El servicio es salvar vidas
                servicio.setType("salva vidas");
                buscar(servicio, "enfermo");
            }

            // Comportamiento para recibir mensajes de respuesta
            addBehaviour(new CyclicBehaviour(this) {
                public void action() {
                    ACLMessage msg = receive();
                    if (msg != null)
                        System.out.println(msg.getSender() + " → " + msg.getContent());
                    else
                        block();
                }
            });
        }
    }

    // Método que realiza la búsqueda en las Páginas Amarillas (DF) de la plataforma
    protected void buscar(final ServiceDescription sd, final String pedido) {
        // Cada 5 segundos intenta buscar agentes que ofrezcan el servicio
        addBehaviour(new TickerBehaviour(this, 5000) {
            protected void onTick() {
                DFAgentDescription dfd = new DFAgentDescription();
                dfd.addServices(sd);

                try {
                    DFAgentDescription[] resultado = DFService.search(myAgent, dfd);
                    if (resultado.length != 0) {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(resultado[0].getName());
                        msg.setContent(pedido);
                        myAgent.send(msg);
                        stop(); // Finaliza el comportamiento cuando encuentra y contacta
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
