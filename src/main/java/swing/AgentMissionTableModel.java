package swing;

import backend.Agent;
import backend.Mission;
import backend.MissionAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AgentMissionTableModel extends AbstractTableModel {

    final static Logger log = LoggerFactory.getLogger(AgentMissionTableModel.class);

    private List<MissionAgent> missionAssignments = new ArrayList<>();

    @Override
    public int getRowCount() {
        return missionAssignments.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {

        ResourceBundle rb = ResourceBundle.getBundle("swing.Bundle");
        switch (columnIndex) {
            case 0:
                return rb.getString("mission");
            case 1:
                return rb.getString("agent");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MissionAgent missionAgent = missionAssignments.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return missionAgent.mission.getCodeName();
            case 1:
                return missionAgent.agent.getName();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addMissionAgent(MissionAgent input) {
        missionAssignments.add(input);
        int lastRow = missionAssignments.size() - 1;

        fireTableRowsInserted(lastRow, lastRow);
    }

    public void editMissionAgent(MissionAgent oldMission, MissionAgent newMission) {
        if (missionAssignments.remove(oldMission)) {
            missionAssignments.add(newMission);
            fireTableDataChanged();
        }
    }

    public void removeAll() {
        this.missionAssignments.clear();
    }

    public List<MissionAgent> getAll() {
        return missionAssignments;
    }

    public MissionAgent getMissionAt(int index) {
        return missionAssignments.get(index);
    }

    public void removeAt(int[] indexes) {
        if (indexes.length > 0) {
            log.info("Missions: " + missionAssignments.size());
            log.info("Selected: " + indexes.length);

            for (Integer i = indexes.length - 1; i >= 0; i--) {
                missionAssignments.remove(indexes[i]);
            }

            int firstRow, lastRow;
            if (indexes.length == 1)
                firstRow = lastRow = indexes[0];
            else {
                firstRow = indexes[0];
                lastRow = indexes[indexes.length - 1];
            }

            fireTableRowsDeleted(firstRow, lastRow);
        }
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        MissionAgent mission = missionAssignments.get(rowIndex);
        switch (columnIndex) {
            case 0:
                mission.agent = ((Agent) aValue);
                break;
            case 1:
                mission.mission = ((Mission) aValue);
                break;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return false;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

}