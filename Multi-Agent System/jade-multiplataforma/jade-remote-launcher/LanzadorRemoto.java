/**
 * $ javac -cp jade.jar agentes/*.java LanzadorRemoto.java
 * $ java -cp "jade.jar:." LanzadorRemoto
 * 
 * */
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class LanzadorRemoto {
    public static void main(String[] args) {
        try {
            Profile p = new ProfileImpl();
            p.setParameter(Profile.MAIN_HOST, "192.168.1.94"); // la IP de tu host
            p.setParameter(Profile.LOCAL_HOST, "192.168.1.94"); // para que el contenedor se anuncie con esa IP
            p.setParameter(Profile.CONTAINER_NAME, "RemoteContainer");
            p.setParameter(Profile.GUI, Boolean.FALSE.toString()); 

            Runtime rt = Runtime.instance();
            AgentContainer container = rt.createAgentContainer(p);

            AgentController agent = container.createNewAgent("RemoteAgent", "agentes.RemoteAgent", null);
            agent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
