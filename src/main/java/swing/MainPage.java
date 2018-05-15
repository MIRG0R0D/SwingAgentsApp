package swing;

import backend.*;
import javax.swing.*;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Properties;
import javax.swing.table.DefaultTableModel;

public class MainPage extends JFrame {

    private static Properties props;
    private static Index index;
    private static JTable tableMissions;
    private static JTable tableAgents ;
    public MainPage(Index index) {
        super("Main page frame");
        this.index = index;
        this.props=index.getProps();
        JFrame frame = new JFrame("Main page frame");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Action exitAction = new AbstractAction(props.getProperty("exit")){//}, loadIcon("exit.png")) {

            {
                putValue(ACCELERATOR_KEY,
                        KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
                putValue(SHORT_DESCRIPTION, "Exit the application");

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu(props.getProperty("file"));
            menuBar.add(fileMenu);

            fileMenu.addSeparator();
            fileMenu.add(exitAction);

        frame.setJMenuBar(menuBar);

        frame.setLayout(new GridBagLayout());
        GridBagConstraints c =  new GridBagConstraints();


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(25, 40);
        textArea.setEditable(false);
        JScrollPane TextScrollPane = new JScrollPane(textArea);

        redrawAgents();
        
        JScrollPane tableAgentsScrollPane = new JScrollPane(tableAgents);


        redrawMissions();
        JScrollPane tableMissionsScrollPane = new JScrollPane(tableMissions);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(props.getProperty("agents"), tableAgentsScrollPane);
        tabbedPane.add(props.getProperty("missions"), tableMissionsScrollPane);

        JButton editButton = new JButton(props.getProperty("edit"));
        JButton newButton = new JButton(props.getProperty("new"));
        JButton deleteButton = new JButton(props.getProperty("delete"));
        JButton refreshButton = new JButton("refresh");

        c.anchor = GridBagConstraints.NORTHEAST; //params of the text area
        c.fill   = GridBagConstraints.BOTH;
        c.gridheight = 2; // how many rows
        c.gridwidth  = 1;
        c.gridx = 0; // where
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1 ;
        c.insets = new Insets(5, 5, 5, 5);
        c.ipadx = 0;
        c.ipady = 0;

        frame.add(TextScrollPane, c);

        c.anchor = GridBagConstraints.NORTHEAST; //params of the text area
        c.fill   = GridBagConstraints.BOTH;
        c.gridheight = 1; // how many rows
        c.gridwidth  = 1;
        c.gridx = 1; // where
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(5, 5, 5, 5);
        frame.add(tabbedPane,c);


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        buttonPanel.add(refreshButton);

        c.anchor = GridBagConstraints.NORTHEAST; //params of the text area
        c.fill   = GridBagConstraints.BOTH;
        c.gridheight = 1; // how many rows
        c.gridwidth  = 1;
        c.gridx = 1; // where
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(5, 5, 5, 5);
        frame.add(buttonPanel,c);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel selLabel = new JLabel("Selected:");
        bottomPanel.add(selLabel);

        final JLabel currentSelectionLabel = new JLabel("");
        currentSelectionLabel.setAutoscrolls(true);
        bottomPanel.add(currentSelectionLabel);

        c.anchor = GridBagConstraints.NORTHEAST; //params of the text area
        c.fill   = GridBagConstraints.BOTH;
        c.gridheight = 1; // how many rows
        c.gridwidth  = 2;
        c.gridx = 0; // where
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(5, 5, 5, 5);
        frame.add(bottomPanel,c);

        //JOptionPane.showMessageDialog(null, "My Goodness, this is so concise");
        newButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                int select = tabbedPane.getSelectedIndex();
                
                if (select == 0) {
                    
                    JFrame secondFrame = new AgentEdit(index, 0);
                }
                else {
                    JFrame secondFrame = new MissionEdit(index, 0);
                }

            }
        } );
        
        refreshButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                redrawAgents();

            }
        } );

        editButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                int selectTab = tabbedPane.getSelectedIndex();

                
                if (selectTab == 0) {
                    Long id;
                    int[] selectedRows = tableAgents.getSelectedRows();
                    if (selectedRows.length == 1) {
                        TableModel model = tableAgents.getModel();
                        id = Long.parseLong((String) model.getValueAt(selectedRows[0], 0));
                    } else {
                        JOptionPane.showMessageDialog(null, props.getProperty("choose_one_row"));
                        return;
                    }
                    JFrame secondFrame = new AgentEdit(index, id);
                } else {
                    Long id;
                    int[] selectedRows = tableMissions.getSelectedRows();
                    if (selectedRows.length == 1) {
                        TableModel model = tableMissions.getModel();
                        id = Long.parseLong((String) model.getValueAt(selectedRows[0], 0));
                    } else {
                        JOptionPane.showMessageDialog(null, props.getProperty("choose_one_row"));
                        return;
                    }
                    JFrame secondFrame = new MissionEdit(index, id);
                }
            }
        });

        deleteButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int select = tabbedPane.getSelectedIndex();

                if (select == 0) {
                    
                    Long id;
                    int[] selectedRows = tableAgents.getSelectedRows();
                    if (selectedRows.length == 1) {
                        TableModel model = tableAgents.getModel();
                        id = Long.parseLong((String) model.getValueAt(selectedRows[0], 0));
                    } else {
                        JOptionPane.showMessageDialog(null, props.getProperty("choose_one_row"));
                        return;
                    }
                    
                    Object[] options = {props.getProperty("yes"), props.getProperty("no")};
                    int n = JOptionPane
                            .showOptionDialog(null, props.getProperty("delete_agent"),
                                    props.getProperty("delete_agent"), JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE, null, options,
                                    options[0]);
                    if (n == 0) {
                        index.getAgentManager().deleteAgentById(id);
                    }

                }

                else {
                    Long id;
                    int[] selectedRows = tableMissions.getSelectedRows();
                    if (selectedRows.length == 1) {
                        TableModel model = tableMissions.getModel();
                        id = Long.parseLong((String) model.getValueAt(selectedRows[0], 0));
                    } else {
                        JOptionPane.showMessageDialog(null, props.getProperty("choose_one_row"));
                        return;
                    }
                    
                    Object[] options = {props.getProperty("yes"), props.getProperty("no")};
                    int n = JOptionPane
                            .showOptionDialog(null, props.getProperty("delete_mission"),
                                    props.getProperty("delete_agent"), JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE, null, options,
                                    options[0]);
                    if (n == 0) {
                        index.getMissionManager().deleteMission(id);
                    }

                }

            }
        } );

        ListSelectionModel selAgentModel = tableAgents.getSelectionModel();

        selAgentModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String result = "";
                int[] selectedRows = tableAgents.getSelectedRows();
                if (selectedRows.length==1){
                    TableModel model = tableAgents.getModel();
                    Long id = Long.parseLong((String) model.getValueAt(selectedRows[0], 0));
                    Agent agent = index.getAgentManager().findAgentById(id);
                    textArea.setText(null);
                    textArea.append("agent id  :"+agent.getId()+System.lineSeparator());
                    textArea.append(props.getProperty("name") +" : "+ agent.getName()+System.lineSeparator());
                    textArea.append(props.getProperty("level") +" : "+ agent.getLevel()+System.lineSeparator());
                    textArea.append(props.getProperty("born") +" : "+ agent.getBornToString()+System.lineSeparator());
                }
                //updating bottom info
                for(int i = 0; i < selectedRows.length; i++) {
                    int selIndex = selectedRows[i];
                    TableModel model = tableAgents.getModel();
                    Object value = model.getValueAt(selIndex, 1);
                    result = result + value;
                    if(i != selectedRows.length - 1) {
                        result += ", ";
                    }
                }
                currentSelectionLabel.setText(result);
            }
        });
        
        ListSelectionModel selMissionModel = tableMissions.getSelectionModel();

        selMissionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String result = "";
                int[] selectedRows = tableMissions.getSelectedRows();
                if (selectedRows.length == 1) {
                    TableModel model = tableMissions.getModel();
                    Long id = Long.parseLong((String) model.getValueAt(selectedRows[0], 0));
                    Mission mission = index.getMissionManager().getMission(id);
                    textArea.setText(null);
                    textArea.append("mission id  :" +mission.getId() + System.lineSeparator());
                    textArea.append(props.getProperty("code_name") + " : " + mission.getCodeName() + System.lineSeparator());
                    textArea.append(props.getProperty("location") + " : " + mission.getLocation() + System.lineSeparator());
                    textArea.append(props.getProperty("description") + " : " + mission.getDescription() + System.lineSeparator());
                }}});
        
                
        frame.setPreferredSize(new Dimension(550, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //System.out.println(Thread.currentThread().toString());
    }
    public static void redrawAgents(){
        String[] agentColumnNames = {
            "Id",
            props.getProperty("name"),
            props.getProperty("level"),
            props.getProperty("born")
        };
        AgentManager am = index.getAgentManager();
        List <Agent> list = am.findAllAgents();
        String[][] result = new String[list.size()][4];
        for (int i=0;i<list.size();i++){
            
            result[i][0] = list.get(i).getId().toString();
            result[i][1] = list.get(i).getName();
            result[i][2] = list.get(i).getLevel();
            result[i][3] = list.get(i).getBornToString();
        }
        TableModel dtm = new DefaultTableModel(result, agentColumnNames);
        tableAgents = new JTable(dtm);
        
    }
    
    public static void redrawMissions(){
        String[] missionColumnNames = {
            
                "id",
                props.getProperty("code_name"),
                props.getProperty("location")
        };
        MissionManager mm = index.getMissionManager();
        List <Mission> list = mm.getMissions();
        String[][] result = new String[list.size()][4];
        for (int i=0;i<list.size();i++){
            
            result[i][0] = list.get(i).getId().toString();
            result[i][1] = list.get(i).getCodeName();
            result[i][2] = list.get(i).getLocation();
            result[i][3] = list.get(i).getDescription();
        }
        tableMissions = new JTable(result, missionColumnNames);
        
    }

/*
    private static void showMainWindow() {
        new MainPage();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(MainPage::showMainWindow);
    }
*/
}