package swing;

import backend.Agent;
import backend.AgentManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

/**
 * Created by Dima on 14.05.2018.
 */
public class AgentEdit extends JFrame {
    private static Properties props;
    private static AgentManager am;
    private static Agent agent;
    

    public AgentEdit(Index index, long id) throws HeadlessException {
        //super("Main page frame");

        this.props = index.getProps();
        
        if (id==0) 
            agent = new Agent();
        else 
            agent=index.getAgentManager().findAgentById(id);

        JFrame frame = new JFrame(props.getProperty("agent_frame"));

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);





        frame.setLayout(new GridBagLayout());
        GridBagConstraints c =  new GridBagConstraints();

        JLabel titleLabel;
        if (id==0) titleLabel= new JLabel(props.getProperty("create_agent"));
        else titleLabel= new JLabel(props.getProperty("edit_agent"));
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

        JLabel nameLabel = new JLabel(props.getProperty("name"));
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=1;
        c.gridx=0;
        frame.add(nameLabel, c);

        JTextField nameField;
        if (id==0) nameField = new JTextField(20);
        else nameField = new JTextField(agent.getName(), 20);
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=1;
        c.gridx=1;
        frame.add(nameField, c);

        JLabel levelLabel = new JLabel(props.getProperty("level"));
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=2;
        c.gridx=0;
        frame.add(levelLabel, c);

        JTextField levelField;
        if (id==0) levelField= new JTextField(20);
        else levelField = new JTextField(agent.getLevel(), 20);
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=2;
        c.gridx=1;
        frame.add(levelField, c);

        JLabel bornLabel = new JLabel(props.getProperty("born"));
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=3;
        c.gridx=0;
        frame.add(bornLabel, c);

        JTextField bornField = new JTextField("finish later!!!",20);
        c.gridwidth=1;
        c.gridheight=1;
        c.gridy=3;
        c.gridx=1;
        frame.add(bornField, c);
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
        c.gridy = 4;
        frame.add(buttonPanel,c);


        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                agent.setName(nameField.getText());
                agent.setLevel(levelField.getText());
                agent.setBorn(LocalDate.now());
                if (agent.getId() == 0) {
                    try {
                        index.getAgentManager().create(agent);
                        JOptionPane.showMessageDialog(null, props.getProperty("create_agent"));
                        frame.dispose();
                    } catch (Exception err) {
                        err.printStackTrace();
                        JOptionPane.showMessageDialog(null, props.getProperty("wrong"));
                    }
                }
                else{
                    try {
                        index.getAgentManager().update(id, agent);
                        JOptionPane.showMessageDialog(null, props.getProperty("update_agent"));
                        frame.dispose();
                    } catch (Exception err) {
                        err.printStackTrace();
                        JOptionPane.showMessageDialog(null, props.getProperty("wrong"));
                    }
                }
            }
        });
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
