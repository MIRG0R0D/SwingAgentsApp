package swing;


import backend.*;
import org.apache.derby.jdbc.ClientDataSource;
import sun.util.ResourceBundleEnumeration;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainWindow {

    private JPanel topPanel;
    private JTabbedPane tabbedPane1;
    private JPanel tabAgents;
    private JButton agentCreateButton;
    private JTable tableAgents;
    private JTable tableMission;
    private JButton agentDeleteButton;
    private JButton agentUpdateButton;
    private JPanel tabMissions;
    private JButton missionCreateButton;
    private JButton missionDeleteButton;
    private JButton missionUpdateButton;
    static MainWindow thisWindow;
    private MissionsTableModel modelMissions;
    private AgentsTableModel modelAgents;
    private boolean isSaveAble = false;

    private static ClientDataSource ds;

    public boolean isSaveAble() {
        return isSaveAble;
    }

    public void setSaveAble(boolean saveAble) {
        isSaveAble = saveAble;
    }

    private AgentManager agentManager;
    private MissionManager missionManager;
    private SecretManager secretManager;

    public AgentManager getAgentManager() {
        return agentManager;
    }

    public MissionManager getMissionManager() {
        return missionManager;
    }

    public MissionsTableModel getModelMissions() {
        return modelMissions;
    }

    public AgentsTableModel getModelAgents() {
        return modelAgents;
    }

    public SecretManager getSecretManager() {
        return secretManager;
    }

    public static ClientDataSource getDataSource() {
        final ResourceBundle rb = ResourceBundle.getBundle("swing.Bundle");
        if (ds == null) {
            ds = new ClientDataSource();
            ds.setServerName(rb.getString("server_name"));
            ds.setPortNumber(Integer.valueOf(rb.getString("port_number")));
            ds.setDatabaseName(rb.getString("database_name"));
        }
        return ds;
    }

    public MainWindow() {

        agentManager = new AgentManagerImpl(getDataSource());
        missionManager = new MissionManagerImpl(getDataSource());
        secretManager = new SecretManagerImpl(getDataSource());

        agentCreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AgentDialog().setVisible(true);
            }
        });

        agentUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableAgents.getSelectedRow() >= 0) {
                    new AgentDialog(modelAgents.getAgentAt(tableAgents.getSelectedRow())).setVisible(true);
                }
            }
        });

        agentDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableAgents.getSelectedRow() >= 0) {
                    modelAgents.removeAt(tableAgents.getSelectedRows());
                }
            }
        });

        missionCreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MissionDialog().setVisible(true);
            }
        });

        missionUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableMission.getSelectedRow() >= 0) {
                    new MissionDialog(modelMissions.getMissionAt(tableMission.getSelectedRow())).setVisible(true);
                }
            }
        });

        missionDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableMission.getSelectedRow() >= 0) {
                    modelMissions.removeAt(tableMission.getSelectedRows());
                }
            }
        });

        setModelsToTables();
    }

    private JMenuBar createMenu() {

        return null;
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void main(String[] args) {
        //Locale.setDefault(Locale.US);
        Locale.setDefault(getLocale());
        //Locale.setDefault(new Locale ("cs","CZ"));
        System.out.println(Locale.getAvailableLocales().toString());

        final ResourceBundle rb = ResourceBundle.getBundle("swing.Bundle");

        thisWindow = new MainWindow();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame(rb.getString("app_name"));
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(thisWindow.topPanel);
                frame.setPreferredSize(new Dimension(800, 600));
                frame.pack();
                frame.setVisible(true);
                frame.setJMenuBar(thisWindow.createMenu());

                centreWindow(frame);
            }
        });

        loadAllDataFromDB();

    }
    public static Locale getLocale(){
        String lang = Locale.getDefault().getLanguage();
        if (lang.equals("ru")) return new Locale("ru","RU");
        if (lang.equals("cs")) return new Locale("cs","CZ");
        if (lang.equals("en")) return new Locale("en","US");
        return new Locale("en","US");
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


    private void setModelsToTables() {
        modelMissions = new MissionsTableModel();
        tableMission.setModel(modelMissions);

        modelAgents = new AgentsTableModel();
        tableAgents.setModel(modelAgents);
    }


    public static void loadAllDataFromDB() {
        LoadAgentsFromDBSwingWorker swingWorker = new LoadAgentsFromDBSwingWorker();
        LoadMissionsFromDBSwingWorker swingWorker1 = new LoadMissionsFromDBSwingWorker();
        swingWorker1.execute();
        swingWorker.execute();
    }


}
