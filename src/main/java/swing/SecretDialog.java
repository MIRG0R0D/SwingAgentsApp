package swing;

import backend.Agent;
import backend.Mission;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SecretDialog extends JDialog {
    private final Mission mission;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel missionNameField;
    private JTable tableWithMission;
    private JTable tableWithoutMission;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private AgentsSecretTableModel agentsWithMission;
    private AgentsSecretTableModel agentsWithoutMission;

    public SecretDialog(Mission input) {
        setPreferredSize(new Dimension(500, 600));

        pack();
        mission = input;
        setContentPane(contentPane);
        setModal(true);


        getRootPane().setDefaultButton(buttonOK);

        missionNameField.setText(mission.getCodeName());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int row = tableWithoutMission.getSelectedRow();
                Agent agent = agentsWithoutMission.getAgentAt(row);
                agentsWithMission.addAgent(agent);
                agentsWithoutMission.removeAt(tableWithoutMission.getSelectedRow());
            }
        });

        buttonRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int row = tableWithMission.getSelectedRow();
                Agent agent = agentsWithMission.getAgentAt(row);
                agentsWithMission.removeAt(row);
                agentsWithoutMission.addAgent(agent);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        agentsWithMission = new AgentsSecretTableModel(true);
        agentsWithMission.missionId = mission.getId();
        agentsWithoutMission = new AgentsSecretTableModel(false);
        tableWithMission.setModel(agentsWithMission);
        tableWithoutMission.setModel(agentsWithoutMission);
        new LoadSecretFromDBSwingWorker(mission.getId(), agentsWithMission, agentsWithoutMission).execute();
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
