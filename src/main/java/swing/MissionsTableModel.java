package swing;

import backend.Mission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MissionsTableModel extends AbstractTableModel{

    final static Logger log = LoggerFactory.getLogger(MissionsTableModel.class);

    private List<Mission> missions = new ArrayList<>();

    @Override
    public int getRowCount() {
        return missions.size();
    }

    @Override
    public int getColumnCount() {
        return Mission.COLUMNS_COUNT;
    }

    @Override
    public String getColumnName(int columnIndex) {

        ResourceBundle rb = ResourceBundle.getBundle("swing.Bundle");
        switch (columnIndex) {
            case 0:
                return rb.getString("codename");
            case 1:
                return rb.getString("start");
            case 2:
                return rb.getString("end");
            case 3:
                return rb.getString("location");
            case 4:
                return rb.getString("description");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return mission.getCodeName();
            case 1:
                return mission.getStart();
            case 2:
                return mission.getEnd();
            case 3:
                return mission.getLocation();
            case 4:
                return mission.getDescription();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
            case 2:
                return LocalDate.class;
            case 3:
            case 4:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addMission(Mission input) {
        missions.add(input);
        int lastRow = missions.size() - 1;

        fireTableRowsInserted(lastRow, lastRow);
    }

    public void editMission(Mission oldMission, Mission newMission)
    {
        if(missions.remove(oldMission))
        {
            missions.add(newMission);
            fireTableDataChanged();
        }
    }

    public void removeAll()
    {
        this.missions.clear();
    }

    public List<Mission> getAll()
    {
        return missions;
    }

    public Mission getMissionAt(int index)
    {
        return missions.get(index);
    }

    public void removeAt(int[] indexes)
    {
        if(indexes.length > 0)
        {
            log.info("Missions: " + missions.size());
            log.info("Selected: " + indexes.length);

            for(Integer i =indexes.length-1;i>=0;i--){
                missions.remove(indexes[i]);
            }

            int firstRow, lastRow;
            if(indexes.length == 1)
                firstRow = lastRow = indexes[0];
            else
            {
                firstRow = indexes[0];
                lastRow = indexes[indexes.length-1];
            }

            fireTableRowsDeleted(firstRow, lastRow);
        }
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                mission.setCodeName((String) aValue);
                break;
            case 1:
                mission.setStart((LocalDate) aValue);
                break;
            case 2:
                mission.setEnd((LocalDate) aValue);
                break;
            case 3:
                mission.setLocation((String) aValue);
                break;
            case 4:
                mission.setDescription((String) aValue);
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
            case 2:
            case 3:
            case 4:
                return true;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

}