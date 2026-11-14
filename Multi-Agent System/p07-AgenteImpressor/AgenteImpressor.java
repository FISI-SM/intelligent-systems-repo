/*
 * Archivo:      AgenteImpressor.java
 *
 * Propósito:    Mostrar un agente que agrega el comportamiento ImprimeFrase
 *               con una pausa fija de 5000 ms entre impresiones.
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
 *       java -cp .:../jade.jar jade.Boot -agents "imp:AgenteImpressor"
 *    Windows CMD:
 *       java -cp .;..\jade.jar jade.Boot -agents "imp:AgenteImpressor"
 *    Windows PowerShell:
 *       java -cp ".;..\jade.jar" jade.Boot -agents "imp:AgenteImpressor"
 *    Para abrir la interfaz gráfica de JADE, agregue -gui antes de -agents.
 *
 * Entrada:      Ninguna.
 *
 * Salida:       El saludo y 10 líneas "& Tiempo <ms>: Mi nombre es imp" (0,
 *               ~5000, ~10000, ...), y al final "imp: ¡Mi comportamiento fue
 *               finalizado!...". Tarda unos 45 segundos.
 *
 * Notas:
 *    1. Compile con *.java: el agente usa ImprimeFrase.java.
 *    2. AgenteImpressorArgs.java es la misma idea, pero la pausa llega como
 *       argumento.
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

public class AgenteImpressor extends Agent {

    protected void setup() {
        System.out.println("¡Hola! Soy un agente impresor!"); 
        System.out.println("# Voy a ejecutar mi comportamiento"); 
        addBehaviour(new ImprimeFrase(this, 5000));
    }
}
