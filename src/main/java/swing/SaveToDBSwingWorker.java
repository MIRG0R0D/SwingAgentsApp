package swing;

import backend.Agent;
import backend.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class SaveToDBSwingWorker extends SwingWorker<Integer, Void> {

    final static Logger log = LoggerFactory.getLogger(SaveToDBSwingWorker.class);

    @Override
    protected Integer doInBackground() throws Exception {
        int counter = 0;

        AgentsTableModel modelAgents = MainWindow.thisWindow.getModelAgents();
        MissionsTableModel modelMissions = MainWindow.thisWindow.getModelMissions();

        MainWindow.thisWindow.getAgentManager().deleteAllAgents();

        for (Agent agent : modelAgents.getAll()) {
            MainWindow.thisWindow.getAgentManager().create(agent);
            counter++;
        }

        log.info("Agents were saved to DB. Count: " + counter);
        counter = 0;


        MainWindow.thisWindow.getMissionManager().deleteAllMissions();

        for (Mission mission : modelMissions.getAll()) {
            MainWindow.thisWindow.getMissionManager().createMission(mission);
            counter++;
        }

        log.info("Missions were saved to DB. Count: " + counter);
        counter = 0;

        return 0;
    }

}
