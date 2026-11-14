/*
 * Archivo:      AgenteImpressorArgs.java
 *
 * Propósito:    Mostrar un agente que recibe como argumento la pausa (en ms)
 *               del comportamiento ImprimeFrase.
 *
 * Compilación (desde la carpeta p07-AgenteImpressor; compila todos los .java):
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       javac -encoding UTF-8 -cp ../jade.jar *.java
 *    Windows CMD:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *    Windows PowerShell:
 *       javac -encoding UTF-8 -cp ..\jade.jar *.java
 *
 * Ejecución:
 *    Linux / macOS (Intel y Apple Silicon M1):
 *       java -cp .:../jade.jar jade.Boot -agents "imp:AgenteImpressorArgs(1000)"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "imp:AgenteImpressorArgs(1000)"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "imp:AgenteImpressorArgs(1000)"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *    Con 1000 ms la ejecución tarda unos 9 segundos.
 *
 * Entrada:      Un entero: la pausa en milisegundos entre impresiones.
 *
 * Salida:       El saludo, 10 líneas "& Tiempo <ms>: Mi nombre es imp"
 *               separadas por la pausa dada y el mensaje de fin. Sin
 *               argumento: "No pasaste argumentos".
 *
 * Notas:
 *    1. El argumento debe ser un número; si no, Long.parseLong lanza
 *       NumberFormatException.
 *    2. Sin argumento el agente no agrega ningún comportamiento, pero sigue
 *       vivo.
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
import jade.core.*;

public class AgenteImpressorArgs extends Agent {
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            long valor = Long.parseLong((String) args[0]);
            System.out.println("¡Hola! Soy un agente impresor!"); 
            System.out.println("# Voy a ejecutar mi comportamiento");
            addBehaviour(new ImprimeFrase(this, valor));
        } else {
            System.out.println("No pasaste argumentos");
        }
    }
}
