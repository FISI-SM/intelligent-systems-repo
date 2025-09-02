import jade.core.Agent;
import jade.core.behaviours.*;

public class AgenteSequencial extends Agent {
	protected void setup() {
		// mensaje de inicialización del agente
		System.out.println("¡Hola! Mi nombre es " + getLocalName());
		System.out.println("Voy a ejecutar tres comportamientos:");

		// creamos un objeto de la clase SequentialBehaviour
		SequentialBehaviour comportamiento = new SequentialBehaviour(this) {
			public int onEnd() {
				myAgent.doDelete();
				return 0;
			}
		};

		// añadimos su primer comportamiento
		comportamiento.addSubBehaviour(new WakerBehaviour(this, 500) {
			long t0 = System.currentTimeMillis();
			protected void onWake() {
				System.out.println((System.currentTimeMillis() - t0) + 
				": ¡Ejecuté mi primer comportamiento después de medio segundo!");
			}
		});

		// añadimos su segundo comportamiento
		comportamiento.addSubBehaviour(new OneShotBehaviour(this) {
			public void action() {
				System.out.println("Ejecuté mi segundo comportamiento");
			}
		});

		// añadimos su tercer comportamiento
		comportamiento.addSubBehaviour(new TickerBehaviour(this, 700) {
			int exec = 0;
			long t1 = System.currentTimeMillis();
			protected void onTick() {
				if (exec == 3) stop();
				else {
					System.out.println((System.currentTimeMillis() - t1) + 
					": Estoy ejecutando mi tercer comportamiento");
					exec++;
				}
			}
		});

		// activamos su ejecución
		addBehaviour(comportamento);
	}

	protected void takeDown() {
		System.out.println("Fui finalizado con éxito");
	}
}
