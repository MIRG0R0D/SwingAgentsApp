package swing;

import backend.Agent;
import backend.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class SaveMissionToDBSwingWorker extends SwingWorker<Integer, Void> {

    final static Logger log = LoggerFactory.getLogger(SaveMissionToDBSwingWorker.class);

    private Mission mission;

    public SaveMissionToDBSwingWorker(Mission mission) {
        this.mission = mission;
    }

    @Override
    protected Integer doInBackground() throws Exception {

        if (mission.getId() != null) {
            MainWindow.thisWindow.getMissionManager().updateMission(mission.getId(), mission);
        } else {
            MainWindow.thisWindow.getMissionManager().createMission(mission);
        }
        return 0;
    }

}
