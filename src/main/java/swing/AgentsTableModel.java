package swing;

import backend.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AgentsTableModel extends AbstractTableModel {

    final static Logger log = LoggerFactory.getLogger(AgentsTableModel.class);

    private List<Agent> agents = new ArrayList<>();

    @Override
    public int getRowCount() {
        return agents.size();
    }

    @Override
    public int getColumnCount() {
        return Agent.COLUMNS_COUNT;
    }

    @Override
    public String getColumnName(int columnIndex) {

        ResourceBundle rb = ResourceBundle.getBundle("swing.Bundle");
        switch (columnIndex) {
            case 0:
                return "id";
            case 1:
                return rb.getString("name");
            case 2:
                return rb.getString("level");
            case 3:
                return rb.getString("born");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agent agent = agents.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return agent.getId();
            case 1:
                return agent.getName();
            case 2:
                return agent.getLevel();
            case 3:
                return agent.getBornToString();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
            case 2:
                return String.class;
            case 3:
                return LocalDate.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addAgent(Agent input) {
        agents.add(input);
        int lastRow = agents.size() - 1;
        new SaveAgentToDBSwingWorker(input).execute();
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void removeAll() {
        this.agents.clear();
    }

    public List<Agent> getAll() {
        return this.agents;
    }

    public void removeAt(int index) {
        new DeleteAgentFromDBSwingWorker(agents.get(index)).execute();
        this.agents.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void removeAt(int[] indexes) {
        if (indexes.length > 0) {
            log.info("Agents: " + agents.size());
            log.info("Selected: " + indexes.length);

            for (Integer i = indexes.length - 1; i >= 0; i--) {
                new DeleteAgentFromDBSwingWorker(agents.get(indexes[i])).execute();
                agents.remove(indexes[i]);
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

    public void editAgent(Agent oldAgent, Agent newAgent) {
        if (agents.remove(oldAgent)) {
            agents.add(newAgent);
            new SaveAgentToDBSwingWorker(newAgent).execute();
            fireTableDataChanged();
        }
    }

    public Agent getAgentAt(int index) {
        return agents.get(index);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Agent agent = agents.get(rowIndex);
        switch (columnIndex) {
            case 0:
                agent.setId((Long) aValue);
                break;
            case 1:
                agent.setName((String) aValue);
                break;
            case 2:
                agent.setLevel((String) aValue);
                break;
            case 3:
                agent.setBorn((LocalDate) aValue);
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
                return false;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

}
