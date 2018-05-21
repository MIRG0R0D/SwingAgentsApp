package swing;

import backend.Agent;
import backend.Mission;
import backend.MissionAgentSecret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class AddAgentToMissionDBSwingWorker extends SwingWorker<MissionAgentSecret, Void> {

    final static Logger log = LoggerFactory.getLogger(AddAgentToMissionDBSwingWorker.class);
    private final Long idMission;
    private final Agent agent;

    public AddAgentToMissionDBSwingWorker(Agent agent, Long idMission) {
        this.idMission = idMission;
        this.agent = agent;
    }

    @Override
    protected MissionAgentSecret doInBackground() throws Exception {
        Mission mission = MainWindow.thisWindow.getMissionManager().getMission(idMission);
        MainWindow.thisWindow.getSecretManager().attachAgentToMission(agent, mission);
        return null;
    }

    @Override
    protected void done() {
    }
}
