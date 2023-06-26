package course_management_swing_ui.view.enrollment;

import course_management_swing_ui.controller.EnrollmentController;
import course_management_swing_ui.model.Module;
import course_management_swing_ui.model.Student;
import course_management_swing_ui.util.MyJTable;
import course_management_swing_ui.view.View;
import course_management_swing_ui.view.ViewManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

import static course_management_swing_ui.repository.DbContext.moduleDbContext;
import static course_management_swing_ui.repository.DbContext.studentDbContext;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the view for Assessment Report. It is also a singleton class to prevent JFrame
 * window spamming
 */
public class AssessmentReportView implements View {
    private static AssessmentReportView instance;
    private EnrollmentController ctrl;

    private JFrame gui;
    private JFrame parentGui;
    private JTable tblEnrollment;
    public JButton btnCheckAll;

    private AssessmentReportView() {

    }

    public static AssessmentReportView getInstance() {
        return instance;
    }

    public static AssessmentReportView getInstance(EnrollmentController ctrl, JFrame parentGui) {
        if (instance == null) {
            instance = new AssessmentReportView();
            instance.ctrl = ctrl;
            instance.parentGui = parentGui;
            instance.onCreate();
        }
        return instance;
    }

    /**
     * This method is called when the view is first created. Similar to onCreate() in Android.
     */
    @Override
    public void onCreate() {
        gui = new JFrame("Assessment report");
        gui.setSize(parentGui.getWidth() + 500, parentGui.getHeight());
        gui.addWindowListener(ctrl);
        ViewManager.setupIcon(this);

        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Close");
        exitMenuItem.addActionListener(ctrl);
        fileMenu.add(exitMenuItem);
        menu.add(fileMenu);

        gui.setJMenuBar(menu);

        // north panel
        JPanel pnlTop = new JPanel();
        pnlTop.setBackground(new Color(0x3e86a0));
        JLabel lblTitle = new JLabel("Assessment report");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 15f));
        pnlTop.add(lblTitle);
        gui.add(pnlTop, BorderLayout.NORTH);

        // center panel
        JPanel pnlMiddle = new JPanel();
        pnlMiddle.setLayout(new BorderLayout());
        gui.add(pnlMiddle);

        Vector<String> headers = new Vector<>(List.of(new String[]{"ID", "Student ID", "Module code", "Internal mark", "Exam mark", "Final grade", ""}));
        Vector<Vector<?>> data = EnrollmentController.dtoAR;

        TableModel tm = new DefaultTableModel(data, headers);
        tblEnrollment = new MyJTable(tm);
        resetTableDimension();
        tblEnrollment.getTableHeader().setReorderingAllowed(false);
        tblEnrollment.setRowHeight(35);
        tblEnrollment.getColumnModel().getColumn(6).setMaxWidth(70);
        ((MyJTable) tblEnrollment).setEditableColumns(true, 1, 2, 3, 4, 5, 6);
        TableColumn stuCol = tblEnrollment.getColumnModel().getColumn(1);
        stuCol.setCellEditor(new DefaultCellEditor(makeStudentComboBox()));

        TableColumn modCol = tblEnrollment.getColumnModel().getColumn(2);
        modCol.setCellEditor(new DefaultCellEditor(makeModuleComboBox()));

        JScrollPane scrContacts = new JScrollPane(tblEnrollment);
        pnlMiddle.add(scrContacts);

        // bottom
        JPanel pnlBottom = new JPanel();
        gui.add(pnlBottom, BorderLayout.SOUTH);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(ctrl);
        pnlBottom.add(btnAdd);

        JButton btnEdit = new JButton("Update");
        btnEdit.addActionListener(ctrl);
        pnlBottom.add(btnEdit);

        btnCheckAll = new JButton("  Check All  ");
        btnCheckAll.addActionListener(ctrl);
        pnlBottom.add(btnCheckAll);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(ctrl);
        pnlBottom.add(btnDelete);

        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(ctrl);
        pnlBottom.add(btnRefresh);

        gui.setLocationRelativeTo(null);
    }

    private JComboBox<?> makeStudentComboBox() {
        String[] students = studentDbContext.stream().map(Student::getId).toArray(String[]::new);
        JComboBox<?> comboBox = new JComboBox<>(students);
        comboBox.setSelectedIndex(-1);
        return comboBox;
    }

    private JComboBox<?> makeModuleComboBox() {
        String[] modules = moduleDbContext.stream().map(Module::getCode).toArray(String[]::new);
        JComboBox<?> comboBox = new JComboBox<>(modules);
        comboBox.setSelectedIndex(-1);
        return comboBox;
    }

    /**
     * @effects return gui
     */
    @Override
    public JFrame getGui() {
        return gui;
    }

    /**
     * @effects return tblEnrollment
     */
    public JTable getTblEnrollment() {
        return tblEnrollment;
    }

    @Override
    public void display() {
        gui.setVisible(true);
    }

    @Override
    public void disposeGUI() {
        gui.dispose();
    }

    @Override
    public void shutDown() {
        disposeGUI();
    }

    private void resetTableDimension() {
        tblEnrollment.setRowHeight(35);
        tblEnrollment.getColumnModel().getColumn(6).setMaxWidth(70);
    }

    /**
     * this method notify the view that data in the logic model has been changed
     */
    @Override
    public void notifyDataChanged() {
        btnCheckAll.setText("  Check All  ");

        Vector<String> headers = new Vector<>(List.of(new String[]{"ID", "Student ID", "Module code", "Internal mark", "Exam mark", "Final grade", ""}));
        Vector<Vector<?>> data = EnrollmentController.dtoAR;
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        tblEnrollment.setModel(tm);
        resetTableDimension();

        TableColumn stuCol = tblEnrollment.getColumnModel().getColumn(1);
        stuCol.setCellEditor(new DefaultCellEditor(makeStudentComboBox()));

        TableColumn modCol = tblEnrollment.getColumnModel().getColumn(2);
        modCol.setCellEditor(new DefaultCellEditor(makeModuleComboBox()));
    }
}
