package course_management_swing_ui.view.enrollment;

import course_management_swing_ui.controller.EnrollmentController;
import course_management_swing_ui.model.Enrollment;
import course_management_swing_ui.model.Module;
import course_management_swing_ui.model.Student;
import course_management_swing_ui.view.View;
import course_management_swing_ui.view.ViewManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static course_management_swing_ui.repository.DbContext.moduleDbContext;
import static course_management_swing_ui.repository.DbContext.studentDbContext;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the view for New enrollment. It is also a singleton class to prevent JFrame
 * window spamming
 */
public class NewEnrollmentView implements View {
    private static NewEnrollmentView instance;
    private EnrollmentController ctrl;

    private JFrame gui;
    private JFrame parentGUI;
    private JTextField txtIm;
    private JTextField txtEm;
    private JComboBox<?> comboBoxStudent;
    private String sid;
    private JComboBox<?> comboBoxModule;
    private String mCode;
    private JButton btnAdd;

    private NewEnrollmentView() {

    }

    public static NewEnrollmentView getInstance() {
        return instance;
    }

    public static NewEnrollmentView getInstance(JFrame parentGUI, EnrollmentController ctrl) {
        if (instance == null) {
            instance = new NewEnrollmentView();
            instance.parentGUI = parentGUI;
            instance.ctrl = ctrl;
            instance.onCreate();
        }
        return instance;
    }

    /**
     * This method is called when the view is first created. Similar to onCreate() in Android.
     */
    @Override
    public void onCreate() {
        gui = new JFrame("New enrollment");
        gui.addWindowListener(ctrl);
        ViewManager.setupIcon(this);

        // center panel
        JPanel pnlMiddle = new JPanel(new GridLayout(5, 2, 5, 10));
        pnlMiddle.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        pnlMiddle.add(new JLabel("Student"));
        String[] students = studentDbContext.stream().map(Student::getId).toArray(String[]::new);
        comboBoxStudent = new JComboBox<>(students);
        comboBoxStudent.setSelectedIndex(-1);
        comboBoxStudent.addActionListener(e -> {
            Object o = comboBoxStudent.getSelectedItem();
            if (o != null) {
                sid = (String) o;
                btnAdd.setEnabled(validateInput());
            }
        });
        pnlMiddle.add(comboBoxStudent);

        pnlMiddle.add(new JLabel("Module"));
        String[] modules = moduleDbContext.stream().map(Module::getCode).toArray(String[]::new);
        comboBoxModule = new JComboBox<>(modules);
        comboBoxModule.setSelectedIndex(-1);
        mCode = null;
        comboBoxModule.addActionListener(e -> {
            Object o = comboBoxModule.getSelectedItem();
            if (o != null) {
                mCode = (String) o;
                btnAdd.setEnabled(validateInput());
            }
        });
        pnlMiddle.add(comboBoxModule);

        pnlMiddle.add(new JLabel("Internal mark"));
        txtIm = new JTextField(15);
        txtIm.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                btnAdd.setEnabled(validateInput());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                btnAdd.setEnabled(validateInput());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                btnAdd.setEnabled(validateInput());
            }
        });
        pnlMiddle.add(txtIm);
        pnlMiddle.add(new JLabel("Examination mark"));
        txtEm = new JTextField(15);
        txtEm.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                btnAdd.setEnabled(validateInput());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                btnAdd.setEnabled(validateInput());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                btnAdd.setEnabled(validateInput());
            }
        });
        pnlMiddle.add(txtEm);
        gui.add(pnlMiddle);

        // bottom
        JPanel pnlBottom = new JPanel();

        btnAdd = new JButton("Add");
        EnrollmentController s = new EnrollmentController(this);
        btnAdd.addActionListener(s);
        pnlBottom.add(btnAdd);
        btnAdd.setEnabled(false);

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
        System.out.println("Add Module GUI displayed...");
    }

    @Override
    public void disposeGUI() {
        sid = null;
        mCode = null;
        txtIm.setText("");
        txtEm.setText("");
        comboBoxStudent.setSelectedIndex(-1);
        comboBoxModule.setSelectedIndex(-1);
        btnAdd.setEnabled(false);
        gui.dispose();
        System.out.println("Add Module GUI disposed...");
    }

    @Override
    public void shutDown() {
        disposeGUI();
    }

    /**
     * @effects return txtName
     */
    public JTextField getTxtIm() {
        return txtIm;
    }

    /**
     * @effects return txtDob
     */
    public JTextField getTxtEm() {
        return txtEm;
    }

    @Override
    public JFrame getGui() {
        return gui;
    }

    public String getSid() {
        return sid;
    }

    public String getmCode() {
        return mCode;
    }

    private boolean validateInput() {
        try {
            return sid != null && mCode != null && Enrollment.validateMark(Double.parseDouble(txtIm.getText())) && Enrollment.validateMark(Double.parseDouble(txtEm.getText()));
        } catch (Exception e) {
            return false;
        }
    }
}
