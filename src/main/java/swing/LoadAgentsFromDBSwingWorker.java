package swing;

import backend.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoadAgentsFromDBSwingWorker extends SwingWorker<List<Agent>, Void> {

    final static Logger log = LoggerFactory.getLogger(LoadAgentsFromDBSwingWorker.class);

    @Override
    protected List<Agent> doInBackground() throws Exception {
        return MainWindow.thisWindow.getAgentManager().findAllAgents();
    }

    @Override
    protected void done() {
        try {
            AgentsTableModel modelAgents = MainWindow.thisWindow.getModelAgents();

            modelAgents.removeAll();



            for (Agent entity : get()) {
                modelAgents.addAgent(entity);
            }

            log.info("Data were loaded from DB and added to models.");

            MainWindow.thisWindow.setSaveAble(true);

        } catch (ExecutionException ex) {
            //TODO log it
        } catch (InterruptedException ex) {
            // K tomuto by v tomto případě nemělo nikdy dojít (viz níže)
            throw new RuntimeException("Operation interrupted (this should never happen)", ex);
        }
    }
}
