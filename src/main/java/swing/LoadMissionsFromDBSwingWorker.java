package swing;

import backend.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoadMissionsFromDBSwingWorker extends SwingWorker<List<Mission>, Void> {

    final static Logger log = LoggerFactory.getLogger(LoadMissionsFromDBSwingWorker.class);

    @Override
    protected List<Mission> doInBackground() throws Exception {
        return MainWindow.thisWindow.getMissionManager().getMissions();
    }

    @Override
    protected void done() {
        try {
            MissionsTableModel modelMissions = MainWindow.thisWindow.getModelMissions();

            modelMissions.removeAll();

            for (Mission entity : get()) {
                modelMissions.addMission(entity);
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
