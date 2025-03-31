package pe.edu.fisi.AgentProject;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.util.Logger;

public class MyAgent extends Agent {
    private Logger myLogger = Logger.getMyLogger(getClass().getName());
    int cont = 0;

    protected void setup() {
        // Register the agent in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("MyAgentType");
        sd.setName("MyAgentName");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Add a behavior (can be customized as needed)
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                // Make a delay in the execution in 0.5 second
                block(500);

                // Example log output
                myLogger.info("Agent running cycle: " + (cont++));
            }
        });
    }
}
