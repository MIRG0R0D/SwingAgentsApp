package swing;

import backend.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class DeleteMissionFromDBSwingWorker extends SwingWorker<Integer, Void> {

    final static Logger log = LoggerFactory.getLogger(DeleteMissionFromDBSwingWorker.class);

    private Mission mission;

    public DeleteMissionFromDBSwingWorker(Mission mission) {
        this.mission = mission;
    }

    @Override
    protected Integer doInBackground() throws Exception {


        MainWindow.thisWindow.getMissionManager().deleteMission(mission.getId());
        return 0;
    }

}
