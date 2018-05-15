package swing;

import backend.Agent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DateTimeException;
import java.time.LocalDate;

public class AgentDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfName;
    private JTextField tfLevel;
    private JTextField tfBorn;
    private Agent inputAgent = null;

    public AgentDialog() {
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(300, 250));
        MainWindow.centreWindow(this);
        pack();
        getRootPane().setDefaultButton(buttonOK);


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
    }

    public AgentDialog(Agent input) {
        this();

        inputAgent = input;

        tfName.setText(inputAgent.getName());
        tfBorn.setText(inputAgent.getBornToString());
        tfLevel.setText(inputAgent.getLevel());
    }

    private void onOK() {

        if (("").equals(tfName.getText())) {
            tfName.setBackground(Color.red);
            return;
        } else {
            tfName.setBackground(Color.white);
        }


        if (("").equals(tfLevel.getText())) {
            tfLevel.setBackground(Color.red);
            return;
        } else {
            tfLevel.setBackground(Color.white);
        }

        LocalDate date;
        try{
            date = LocalDate.parse(tfBorn.getText());
        }catch (DateTimeException e){
            tfBorn.setBackground(Color.red);
            return;
        }

        tfBorn.setBackground(Color.white);


        if (inputAgent == null) {
            Agent tmp = new Agent(null, date,tfLevel.getText(), tfName.getText());
            MainWindow.thisWindow.getModelAgents().addAgent(tmp);
        } else {
            Agent tmp = new Agent(inputAgent.getId(), date,tfLevel.getText(), tfName.getText());
            MainWindow.thisWindow.getModelAgents().editAgent(inputAgent, tmp);
        }


        dispose();
    }

    private void onCancel() {
        dispose();
    }


}
