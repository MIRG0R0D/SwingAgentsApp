package swing;


import backend.*;
import org.apache.derby.jdbc.ClientDataSource;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;


public class MainWindow {

    private JPanel topPanel;
    private JTabbedPane tabbedPane1;
    private JPanel tabAgents;
    private JButton agentCreateButton;
    private JTable tableSecret;
    private JTable tableAgents;
    private JTable tableMission;
    private JButton agentDeleteButton;
    private JButton agentUpdateButton;
    private JPanel tabMissions;
    private JButton missionCreateButton;
    private JButton missionDeleteButton;
    private JButton missionUpdateButton;
    private JPanel tabAgentMission;
    private JButton secretCreateButton;
    private JButton secretDeleteButton;
    private JButton secretUpdateButton;
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
    private SecretManager sercretManager;

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

    public static ClientDataSource getDataSource() {
        if (ds == null) {
            ds = new ClientDataSource();
            ds.setServerName("localhost");
            ds.setPortNumber(1527);
            ds.setDatabaseName("myDB");
        }
        return ds;
    }

    public MainWindow() {

        agentManager = new AgentManagerImpl(getDataSource());
        missionManager = new MissionManagerImpl(getDataSource());

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
        setEditableCells();
    }

    private JMenuBar createMenu() {
        //hlavní úroveň menu
//        ResourceBundle rb = ResourceBundle.getBundle("swing.Bundle");
//        JMenuBar menuBar = new JMenuBar();
//        JMenu fileMenu = new JMenu(rb.getString("File"));
//        final JMenu helpMenu = new JMenu(rb.getString("Help"));
//        JMenu lafMenu = new JMenu(rb.getString("LaF"));
//        menuBar.add(fileMenu);
//        menuBar.add(lafMenu);
//        menuBar.add(helpMenu);
//        menuBar.add(Box.createHorizontalGlue());
//
//        //menu File
//        JMenuItem exitMenuItem = new JMenuItem(rb.getString("Exit"));
//        fileMenu.add(exitMenuItem);
//        exitMenuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.exit(1);
//            }
//        });
//        //menu Help
//        JMenuItem aboutMenuItem = new JMenuItem("About");
//        helpMenu.add(aboutMenuItem);
//        aboutMenuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(helpMenu, "Skvělá aplikace (c) Já", "About", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//        for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//            JMenuItem item = new JMenuItem(info.getName());
//            lafMenu.add(item);
//            item.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent ev) {
//                    try {
//                        UIManager.setLookAndFeel(info.getClassName());
//                        SwingUtilities.updateComponentTreeUI(MainWindow.this.topPanel);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            });
//        }
        return null;
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void main(String[] args) {
        final ResourceBundle rb = ResourceBundle.getBundle("swing.Bundle");
        thisWindow = new MainWindow();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Secret");
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

        thisWindow.getModelAgents().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (thisWindow.isSaveAble())
                    saveAllDataToDB();
            }
        });

        thisWindow.getModelMissions().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (thisWindow.isSaveAble())
                    saveAllDataToDB();
            }
        });
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

    private void setEditableCells() {
//        JComboBox genreComboBox = new JComboBox();
//        for (GenreEnum f : GenreEnum.values()) {
//            genreComboBox.addItem(f);
//        }
//        tableAgents.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(genreComboBox));
//        tableMissions.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(genreComboBox));
//
//        JComboBox kindComboBox = new JComboBox();
//        for (KindEnum f : KindEnum.values()) {
//            kindComboBox.addItem(f);
//        }
//        tableMissions.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(kindComboBox));
//
//        JComboBox typeComboBox = new JComboBox();
//        for (TypeEnum f : TypeEnum.values()) {
//            typeComboBox.addItem(f);
//        }
//        tableMissions.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(typeComboBox));
    }

    public static void loadAllDataFromDB() {
        LoadAgentsFromDBSwingWorker swingWorker = new LoadAgentsFromDBSwingWorker();
        LoadMissionsFromDBSwingWorker swingWorker1 = new LoadMissionsFromDBSwingWorker();
        swingWorker1.execute();
        swingWorker.execute();
    }

    public static void saveAllDataToDB() {
        SaveToDBSwingWorker swingWorker = new SaveToDBSwingWorker();
        swingWorker.execute();
    }


}
