package swing;

import backend.Agent;
import backend.Mission;
import backend.MissionAgentSecret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoadSecretFromDBSwingWorker extends SwingWorker<MissionAgentSecret, Void> {

    final static Logger log = LoggerFactory.getLogger(LoadSecretFromDBSwingWorker.class);
    private final Long idMission;
    private final AgentsSecretTableModel agentsWithMission;
    private final AgentsSecretTableModel agentsWithoutMission;


    public LoadSecretFromDBSwingWorker(Long idMission, AgentsSecretTableModel agentsWithMission, AgentsSecretTableModel agentsWithoutMission) {
        this.idMission = idMission;
        this.agentsWithMission = agentsWithMission;
        this.agentsWithoutMission = agentsWithoutMission;
    }

    @Override
    protected MissionAgentSecret doInBackground() throws Exception {
        Mission mission = MainWindow.thisWindow.getMissionManager().getMission(idMission);
        List<Agent> agentsWithMission = MainWindow.thisWindow.getSecretManager().findAgentsWithMission(mission);
        List<Agent> agentsWithoutMission = MainWindow.thisWindow.getAgentManager().findAllAgents();
        for (Agent agent : agentsWithMission) {
            agentsWithoutMission.remove(agent);
        }
        return new MissionAgentSecret(mission, agentsWithMission, agentsWithoutMission);
    }

    @Override
    protected void done() {

        try {
            agentsWithMission.saveToDB = false;
            for (Agent agent : get().agentsWithMission) {
                agentsWithMission.addAgent(agent);
            }
            agentsWithMission.saveToDB = true;

            for (Agent agent : get().agentsWithoutMission) {
                agentsWithoutMission.addAgent(agent);
            }
            log.info("Data were loaded from DB and added to models.");
        } catch (InterruptedException e) {
            log.info(e.toString());
        } catch (ExecutionException e) {
            log.info(e.toString());
        }


    }
}
