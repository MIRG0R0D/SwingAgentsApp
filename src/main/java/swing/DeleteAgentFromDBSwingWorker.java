package swing;

import backend.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class DeleteAgentFromDBSwingWorker extends SwingWorker<Integer, Void> {

    final static Logger log = LoggerFactory.getLogger(DeleteAgentFromDBSwingWorker.class);

    private Agent agent;

    public DeleteAgentFromDBSwingWorker(Agent agent) {
        this.agent = agent;
    }

    @Override
    protected Integer doInBackground() throws Exception {


        MainWindow.thisWindow.getAgentManager().deleteAgentById(agent.getId());

        return 0;
    }

}
