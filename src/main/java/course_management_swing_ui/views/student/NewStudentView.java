package course_management_swing_ui.views.student;

import course_management_swing_ui.controllers.StudentController;
import course_management_swing_ui.views.View;
import course_management_swing_ui.views.ViewManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the views for "New student". It is also a singleton class to prevent JFrame
 * window spamming
 */
public class NewStudentView implements View {
    private static NewStudentView instance;
    private StudentController ctrl;

    private JFrame gui;
    private JFrame parentGUI;
    private JTextField txtName;
    private JTextField txtDob;
    private JTextField txtAddress;
    private JTextField txtEmail;

    private NewStudentView() {

    }

    public static NewStudentView getInstance() {
        return instance;
    }

    public static NewStudentView getInstance(JFrame parentGUI, StudentController ctrl) {
        if (instance == null) {
            instance = new NewStudentView();
            instance.parentGUI = parentGUI;
            instance.ctrl = ctrl;
            instance.onCreate();
        }
        return instance;
    }

    /**
     * This method is called when the views is first created. Similar to onCreate() in Android.
     */
    @Override
    public void onCreate() {
        gui = new JFrame("New student");
        gui.addWindowListener(ctrl);
        ViewManager.setupIcon(this);

        // center panel
        JPanel pnlMiddle = new JPanel(new GridLayout(4, 2, 5, 10));
        pnlMiddle.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        pnlMiddle.add(new JLabel("Name"));
        txtName = new JTextField(15);
        pnlMiddle.add(txtName);
        pnlMiddle.add(new JLabel("Date of Birth (YYYY-MM-DD)"));
        txtDob = new JTextField(15);
        pnlMiddle.add(txtDob);
        pnlMiddle.add(new JLabel("Address"));
        txtAddress = new JTextField(15);
        pnlMiddle.add(txtAddress);
        pnlMiddle.add(new JLabel("Email"));
        txtEmail = new JTextField(15);
        pnlMiddle.add(txtEmail);

        gui.add(pnlMiddle);

        // bottom
        JPanel pnlBottom = new JPanel();

        JButton btnAdd = new JButton("Add");
        StudentController s = new StudentController(this);
        btnAdd.addActionListener(s);
        pnlBottom.add(btnAdd);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(s);
        pnlBottom.add(btnCancel);

        pnlBottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        gui.add(pnlBottom, BorderLayout.SOUTH);
        gui.pack();

        int x = (int) parentGUI.getLocation().getX() + 100;
        int y = (int) parentGUI.getLocation().getY() + 100;
        gui.setLocation(x, y);
    }

    @Override
    public void display() {
        gui.setVisible(true);
        System.out.println("Add Contact GUI displayed...");
    }

    @Override
    public void disposeGUI() {
        txtName.setText("");
        txtDob.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        gui.dispose();
        System.out.println("Add Contact GUI disposed...");
    }

    @Override
    public void shutDown() {
        disposeGUI();
    }

    /**
     * @effects return txtName
     */
    public JTextField getTxtName() {
        return txtName;
    }

    /**
     * @effects return txtDob
     */
    public JTextField getTxtDob() {
        return txtDob;
    }

    /**
     * @effects return txtAddress
     */
    public JTextField getTxtAddress() {
        return txtAddress;
    }

    /**
     * @effects return txtPhone
     */
    public JTextField getTxtEmail() {
        return txtEmail;
    }

    @Override
    public JFrame getGui() {
        return gui;
    }
}
