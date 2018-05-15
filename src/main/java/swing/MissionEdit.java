package swing;

import backend.Mission;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Properties;

/**
 * Created by Dima on 14.05.2018.
 */
public class MissionEdit extends JFrame {
    private static Properties props;
    private static Index index;
    private static Mission mission;

    public MissionEdit(Index index, long id) throws HeadlessException {
        //super("Main page frame");

        this.index = index;
        this.props = index.getProps();
        
        if (id==0) mission = new Mission();
        else mission=index.getMissionManager().getMission(id);
        
        

        JFrame frame = new JFrame(props.getProperty("mission_frame"));

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);





        frame.setLayout(new GridBagLayout());
        GridBagConstraints c =  new GridBagConstraints();

        JLabel titleLabel;
        if (id==0) titleLabel= new JLabel(props.getProperty("create_mission"));
        else titleLabel= new JLabel(props.getProperty("edit_mission"));
        c.anchor = GridBagConstraints.CENTER; //params of the text area
        c.fill   = GridBagConstraints.BOTH;
        c.gridheight = 1; // how many rows
        c.gridwidth  = 2;
        c.gridx = 0; // where
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0 ;
        c.insets = new Insets(5, 5, 5, 5);
        c.ipadx = 0;
        c.ipady = 0;
        frame.add(titleLabel, c);

        JLabel nameLabel = new JLabel(props.getProperty("code_name"));
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=1;
        c.gridx=0;
        frame.add(nameLabel, c);

        JTextField nameField ;
        if (id==0) nameField= new JTextField(20);
        else nameField = new JTextField(mission.getCodeName(), 20);
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=1;
        c.gridx=1;
        frame.add(nameField, c);

        JLabel locationLabel = new JLabel(props.getProperty("location"));
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=2;
        c.gridx=0;
        frame.add(locationLabel, c);

        JTextField locationField ;
        if (id==0) locationField= new JTextField(20);
        else locationField= new JTextField(mission.getLocation(),20);
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=2;
        c.gridx=1;
        frame.add(locationField, c);
        
        JLabel descriptionLabel = new JLabel(props.getProperty("description"));
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=3;
        c.gridx=0;
        frame.add(descriptionLabel, c);

        JTextField descriptionField ;
        if (id==0) descriptionField= new JTextField(20);
        else descriptionField= new JTextField(mission.getDescription(),20);
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=3;
        c.gridx=1;
        frame.add(descriptionField, c);
        
        JLabel startLabel = new JLabel(props.getProperty("start"));
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=4;
        c.gridx=0;
        frame.add(startLabel, c);

        JTextField startField = new JTextField("finish later!!!",20);
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=4;
        c.gridx=1;
        frame.add(startField, c);

        JLabel endLabel = new JLabel(props.getProperty("end"));
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=5;
        c.gridx=0;
        frame.add(endLabel, c);

        JTextField endField = new JTextField("finish later!!!",20);
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=5;
        c.gridx=1;
        frame.add(endField, c);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton(props.getProperty("save"));
        JButton cancelButton = new JButton(props.getProperty("cancel"));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        c.anchor = GridBagConstraints.CENTER; //params of the text area
        c.fill   = GridBagConstraints.BOTH;
        c.gridheight = 1; // how many rows
        c.gridwidth  = 2;
        c.gridx = 0; // where
        c.gridy = 6;
        frame.add(buttonPanel,c);
        
        
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                mission.setCodeName(nameField.getText());
                mission.setDescription(descriptionField.getText());
                mission.setLocation(locationField.getText());
                mission.setStart(LocalDate.now());
                mission.setEnd(LocalDate.now());
                if (mission.getId() == 0) {
                    try {
                        index.getMissionManager().createMission(mission);
                        JOptionPane.showMessageDialog(null, props.getProperty("new_mission"));
                        frame.dispose();
                    } catch (Exception err) {
                        err.printStackTrace();
                        JOptionPane.showMessageDialog(null, props.getProperty("wrong"));
                    }
                }
                else{
                    try {
                        index.getMissionManager().updateMission(id, mission);
                        JOptionPane.showMessageDialog(null, props.getProperty("update_mission"));
                        frame.dispose();
                    } catch (Exception err) {
                        err.printStackTrace();
                        JOptionPane.showMessageDialog(null, props.getProperty("wrong"));
                    }
                }
            }});
        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }});

        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //System.out.println(Thread.currentThread().toString());
    }
/*
    private static void showAgentEdit() {
        new AgentEdit(0);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(AgentEdit::showAgentEdit);
    }
*/
}
