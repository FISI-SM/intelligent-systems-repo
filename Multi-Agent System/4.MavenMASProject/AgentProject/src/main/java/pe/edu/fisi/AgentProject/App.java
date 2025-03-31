package pe.edu.fisi.AgentProject;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.StaleProxyException;
import jade.wrapper.AgentContainer;

public class App {

	/*
	public static void main2(String[] args) {
		Runtime runtime = Runtime.instance();
		ContainerController container = runtime.createMainContainer(new Container());
		try {
			AgentController agent = container.createNewAgent("ExampleAgent", HelloAgent.class.getName(), new Object[0]);
			agent.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/

	public static void main(String[] args) {
		Properties pp = new Properties();
		pp.setProperty(Profile.GUI, Boolean.TRUE.toString());
		Profile p = new ProfileImpl(pp);
		AgentContainer ac = Runtime.instance().createMainContainer(p);

		try {
			ac.acceptNewAgent("MyAgentMqtt", new MyAgent()).start();
			//ac.acceptNewAgent("Hello", new HelloAgent()).start();
			//ac.acceptNewAgent("Hi", new HelloAgent()).start();
		} catch (StaleProxyException e) {
			throw new Error(e);
		}
	}

}
