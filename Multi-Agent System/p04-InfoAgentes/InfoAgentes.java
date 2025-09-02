/**
 * Para compilar este código:
 * $ javac HelloAgent.java
 * $ java -cp "jade.jar:classes:." jade.Boot -gui tuto:InfoAgentes
 * $ java -cp "jade.jar:classes:." jade.Boot -name plataforma-de-prueba Ivan:InfoAgentes
 **/
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

