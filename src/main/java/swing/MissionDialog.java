package swing;

import backend.Mission;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DateTimeException;
import java.time.LocalDate;

public class MissionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonAgents;
    private JTextField tfCodeName;
    private JTextField tfStart;
    private JTextField tfEnd;
    private JTextField tfLocation;
    private JTextField tfDescription;
    private Mission inputMission = null;

    public MissionDialog() {
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

        buttonAgents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SecretDialog(inputMission).setVisible(true);
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

    public MissionDialog(Mission input) {
        this();

        inputMission = input;

        tfCodeName.setText(inputMission.getCodeName());
        tfStart.setText(inputMission.getStart().toString());
        if (inputMission.getEnd() != null)
            tfEnd.setText(inputMission.getEnd().toString());
        tfDescription.setText(inputMission.getDescription());
        tfLocation.setText(inputMission.getLocation());
    }

    private void onOK() {

        if (("").equals(tfCodeName.getText())) {
            tfCodeName.setBackground(Color.red);
            return;
        } else {
            tfCodeName.setBackground(Color.white);
        }


        if (("").equals(tfLocation.getText())) {
            tfLocation.setBackground(Color.red);
            return;
        } else {
            tfLocation.setBackground(Color.white);
        }

        if (("").equals(tfDescription.getText())) {
            tfDescription.setBackground(Color.red);
            return;
        } else {
            tfDescription.setBackground(Color.white);
        }

        LocalDate dateStart;
        try {
            dateStart = LocalDate.parse(tfStart.getText());
        } catch (DateTimeException e) {
            tfStart.setBackground(Color.red);
            return;
        }

        tfStart.setBackground(Color.white);

        LocalDate dateEnd = null;
        if (tfEnd.getText().trim().length() > 0) {
            try {
                dateEnd = LocalDate.parse(tfEnd.getText());
            } catch (DateTimeException e) {
                tfEnd.setBackground(Color.red);
                return;
            }
        }

        tfStart.setBackground(Color.white);

        if (inputMission == null) {
            Mission tmp = new Mission(null, tfCodeName.getText(), dateStart, dateEnd, tfLocation.getText(), tfDescription.getText());
            MainWindow.thisWindow.getModelMissions().addMission(tmp);
        } else {
            Mission tmp = new Mission(inputMission.getId(), tfCodeName.getText(), dateStart, dateEnd, tfLocation.getText(), tfDescription.getText());
            MainWindow.thisWindow.getModelMissions().editMission(inputMission, tmp);
        }


        dispose();
    }

    private void onCancel() {
        dispose();
    }


}
