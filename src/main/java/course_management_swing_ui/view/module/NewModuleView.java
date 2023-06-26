package course_management_swing_ui.view.module;

import course_management_swing_ui.controller.ModuleController;
import course_management_swing_ui.model.Module;
import course_management_swing_ui.view.View;
import course_management_swing_ui.view.ViewManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the view for "New module". It is also a singleton class to prevent JFrame
 * window spamming
 */
public class NewModuleView implements View {
    private static NewModuleView instance;
    private ModuleController ctrl;

    private JFrame gui;
    private JFrame parentGUI;
    private JTextField txtName;
    private JTextField txtSemester;
    private JTextField txtCredits;
    private JTextField txtDepartment;
    private JComboBox<?> comboBoxMt;
    private Module.ModuleType moduleType;
    private JButton btnAdd;

    private NewModuleView() {

    }

    public static NewModuleView getInstance() {
        return instance;
    }

    public static NewModuleView getInstance(JFrame parentGUI, ModuleController ctrl) {
        if (instance == null) {
            instance = new NewModuleView();
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
        gui = new JFrame("New module");
        gui.addWindowListener(ctrl);
        ViewManager.setupIcon(this);

        // center panel
        JPanel pnlMiddle = new JPanel(new GridLayout(5, 2, 5, 10));
        pnlMiddle.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        pnlMiddle.add(new JLabel("Name"));
        txtName = new JTextField(15);
        pnlMiddle.add(txtName);
        pnlMiddle.add(new JLabel("Semester"));
        txtSemester = new JTextField(15);
        pnlMiddle.add(txtSemester);
        pnlMiddle.add(new JLabel("Credits"));
        txtCredits = new JTextField(15);
        pnlMiddle.add(txtCredits);

        pnlMiddle.add(new JLabel("Module type"));
        Module.ModuleType[] moduleTypes = {Module.ModuleType.COMPULSORY, Module.ModuleType.ELECTIVE};
        comboBoxMt = new JComboBox<>(moduleTypes);
        comboBoxMt.setSelectedIndex(-1);
        moduleType = null;
        comboBoxMt.addActionListener(e -> {
            if (comboBoxMt.getSelectedItem() == Module.ModuleType.ELECTIVE) {
                moduleType = Module.ModuleType.ELECTIVE;
                NewModuleView.getInstance().txtDepartment.setEditable(true);
                btnAdd.setEnabled(true);
            } else if (comboBoxMt.getSelectedItem() == Module.ModuleType.COMPULSORY) {
                moduleType = Module.ModuleType.COMPULSORY;
                NewModuleView.getInstance().txtDepartment.setEditable(false);
                btnAdd.setEnabled(true);
            } else {
                btnAdd.setEnabled(false);
            }
        });
        pnlMiddle.add(comboBoxMt);

        pnlMiddle.add(new JLabel("Department"));
        txtDepartment = new JTextField(15);
        pnlMiddle.add(txtDepartment);
        txtDepartment.setEditable(false);

        gui.add(pnlMiddle);

        // bottom
        JPanel pnlBottom = new JPanel();

        btnAdd = new JButton("Add");
        ModuleController s = new ModuleController(this);
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
        txtName.setText("");
        txtSemester.setText("");
        txtCredits.setText("");
        comboBoxMt.setSelectedIndex(-1);
        txtDepartment.setText("");
        txtDepartment.setEditable(false);
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
    public JTextField getTxtName() {
        return txtName;
    }

    /**
     * @effects return txtDob
     */
    public JTextField getTxtSemester() {
        return txtSemester;
    }

    /**
     * @effects return txtAddress
     */
    public JTextField getTxtCredits() {
        return txtCredits;
    }

    /**
     * @effects return txtDepartment
     */
    public JTextField getTxtDepartment() {
        return txtDepartment;
    }

    /**
     * @effects return comboBoxModuleType
     */
    public Module.ModuleType getModuleType() {
        return moduleType;
    }

    @Override
    public JFrame getGui() {
        return gui;
    }
}
