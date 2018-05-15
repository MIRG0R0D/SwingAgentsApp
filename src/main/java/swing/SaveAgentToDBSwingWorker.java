package swing;

import backend.Agent;
import backend.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class SaveAgentToDBSwingWorker extends SwingWorker<Integer, Void> {

    final static Logger log = LoggerFactory.getLogger(SaveAgentToDBSwingWorker.class);

    private Agent agent;

    public SaveAgentToDBSwingWorker(Agent agent) {
        this.agent = agent;
    }

    @Override
    protected Integer doInBackground() throws Exception {

        if (agent.getId() != null) {
            MainWindow.thisWindow.getAgentManager().update(agent.getId(), agent);
        } else {
            MainWindow.thisWindow.getAgentManager().create(agent);
        }
        return 0;
    }

}
