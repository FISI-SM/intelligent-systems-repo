/*
 * Archivo:      InfoAgentes.java
 *
 * Propósito:    Mostrar la identidad de un agente JADE (AID): su nombre
 *               local, su nombre global (GUID) y sus direcciones de
 *               transporte.
 *
 * Compilación (desde la carpeta p04-InfoAgentes; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "tuto:InfoAgentes"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "tuto:InfoAgentes"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "tuto:InfoAgentes"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *    Con -name plataforma-de-prueba antes de -agents, el GUID cambia de
 *    tuto@<host>:1099/JADE a tuto@plataforma-de-prueba.
 *
 * Entrada:      Ninguna.
 *
 * Salida:       El AID completo, "Mi nombre local es tuto", "Mi nombre global
 *               (GUID) es tuto@<host>:1099/JADE" y la lista de direcciones
 *               (por ejemplo, http://<host>:7778/acc).
 *
 * Notas:
 *    1. El nombre local es único dentro de la plataforma; el GUID es único
 *       entre plataformas: nombre-local@nombre-de-la-plataforma.
 *    2. Las direcciones dependen de la red de la máquina, por eso cambian
 *       entre computadoras.
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
import jade.core.AID;
import java.util.Iterator;

public class InfoAgentes extends Agent {

    protected void setup(){
        System.out.println("Hola Mundo. ¡Soy un agente!");
        System.out.println("Toda mi información: \n" + getAID());
        System.out.println("Mi nombre local es " + getAID().getLocalName());
        System.out.println("Mi nombre global (GUID) es " + getAID().getName());
        System.out.println("Mis direcciones son:");
        Iterator it = getAID().getAllAddresses();
        
        while(it.hasNext()) {
            System.out.println("- " + it.next());
        }
    }
}

