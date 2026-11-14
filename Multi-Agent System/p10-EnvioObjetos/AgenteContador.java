/*
 * Archivo:      AgenteContador.java
 *
 * Propósito:    Mostrar cómo un agente recibe objetos Java dentro de mensajes
 *               ACL con getContentObject() y los imprime.
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
 *       java -cp .:../jade.jar jade.Boot -agents "Contador:AgenteContador;Estoque:AgenteEstoque"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "Contador:AgenteContador;Estoque:AgenteEstoque"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "Contador:AgenteContador;Estoque:AgenteEstoque"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Mensajes ACL con objetos Musicos.
 *
 * Salida:       "Agente Contador inicializado.", "Esperando información..." y
 *               los datos (nombre, edad, banda) de los 5 músicos que envía
 *               AgenteEstoque.
 *
 * Notas:
 *    1. El nombre local debe ser Contador: AgenteEstoque envía a ese nombre.
 *    2. Inicie Contador antes que Estoque en -agents (el orden de la lista).
 *    3. El arreglo tiene 5 posiciones; un sexto objeto causaría una excepción
 *       y el contador volvería a 0.
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
