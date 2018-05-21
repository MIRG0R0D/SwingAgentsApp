package swing;

import backend.Agent;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Properties;

public class AgentDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfName;
    private JTextField tfLevel;
    private JPanel panelBirth;
    private JTextField tfBorn;
    private Agent inputAgent = null;


    private JDatePickerImpl datePicker;

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

        datePicker.getModel().setDay(input.getBorn().getDayOfMonth());
        datePicker.getModel().setMonth(input.getBorn().getMonthValue()-1);
        datePicker.getModel().setYear(input.getBorn().getYear());
        datePicker.getModel().setSelected(true);


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

        //System.out.println("hello new agent");
        //System.out.println("Birth date = "+datePicker.getJFormattedTextField().getText());
        try{
            date = LocalDate.parse(datePicker.getJFormattedTextField().getText());
        }catch (DateTimeException e){
            //tfBorn.setBackground(Color.red);
            return;
        }

        //System.out.println("date ok");



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


    private void createUIComponents() {
        // TODO: place custom component creation code here

        System.out.println("CreateUI");
        UtilDateModel model = new UtilDateModel();

        //model.setDate(1990, 8, 24);

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");



        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...

        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        //datePicker.getModel().setDate(2012, 1,1);

        //datePicker.getModel().setSelected(true);


        panelBirth = datePicker;

    }
}
