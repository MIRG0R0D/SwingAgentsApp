package swing;

import backend.Mission;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Properties;

public class MissionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonAgents;
    private JTextField tfCodeName;
    private JTextField tfLocation;
    private JTextField tfDescription;
    private JPanel panelEnd;
    private JPanel panelStart;
    private Mission inputMission = null;

    private JDatePickerImpl datePickerStart;
    private JDatePickerImpl datePickerEnd;

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
        //tfStart.setText(inputMission.getStart().toString());
        datePickerStart.getModel().setDay(input.getStart().getDayOfMonth());
        datePickerStart.getModel().setMonth(input.getStart().getMonthValue()-1);
        datePickerStart.getModel().setYear(input.getStart().getYear());
        datePickerStart.getModel().setSelected(true);


        if (inputMission.getEnd() != null) {
            //tfEnd.setText(inputMission.getEnd().toString());
            datePickerEnd.getModel().setDay(input.getEnd().getDayOfMonth());
            datePickerEnd.getModel().setMonth(input.getEnd().getMonthValue() - 1);
            datePickerEnd.getModel().setYear(input.getEnd().getYear());
            datePickerEnd.getModel().setSelected(true);
        }


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
            dateStart = LocalDate.parse(datePickerStart.getJFormattedTextField().getText());
            //dateStart = LocalDate.parse(tfStart.getText());
        } catch (DateTimeException e) {
            datePickerStart.setBackground(Color.red);
            return;
        }

        datePickerStart.setBackground(Color.white);

        LocalDate dateEnd = null;
        if (datePickerEnd.getJFormattedTextField().getText().length() > 0) {
            try {
                dateEnd = LocalDate.parse(datePickerEnd.getJFormattedTextField().getText());
            } catch (DateTimeException e) {
                datePickerEnd.setBackground(Color.red);
                return;
            }
        }

        datePickerEnd.setBackground(Color.white);

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


    private void createUIComponents() {
        UtilDateModel modelStart = new UtilDateModel();
        UtilDateModel modelEnd = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStart, p);
        JDatePanelImpl datePanelEnd = new JDatePanelImpl(modelEnd, p);

        datePickerStart = new JDatePickerImpl(datePanelStart, new DateLabelFormatter());
        datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
        panelStart = datePickerStart;
        panelEnd = datePickerEnd;

    }
}
