package a2_1901040191.view.enrollment;

import a2_1901040191.controller.EnrollmentController;
import a2_1901040191.controller.ModuleController;
import a2_1901040191.controller.StudentController;
import a2_1901040191.model.Module;
import a2_1901040191.model.Student;
import a2_1901040191.util.MyJTable;
import a2_1901040191.view.View;
import a2_1901040191.view.ViewManager;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

import static a2_1901040191.repository.DbContext.moduleDbContext;
import static a2_1901040191.repository.DbContext.studentDbContext;
/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the view for Initial Report. It is also a singleton class to prevent JFrame
 * window spamming
 */
public class InitialReportView implements View {
    private static InitialReportView instance;
    private EnrollmentController ctrl;

    private JFrame gui;
    private JFrame parentGui;
    private JTable tblEnrollment;
    public JButton btnCheckAll;

    private InitialReportView() {

    }

    public static InitialReportView getInstance() {
        return instance;
    }

    public static InitialReportView getInstance(EnrollmentController ctrl, JFrame parentGui) {
        if (instance == null) {
            instance = new InitialReportView();
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
        gui = new JFrame("Initial report");
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
        JLabel lblTitle = new JLabel("Initial report");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 15f));
        pnlTop.add(lblTitle);
        gui.add(pnlTop, BorderLayout.NORTH);

        // center panel
        JPanel pnlMiddle = new JPanel();
        pnlMiddle.setLayout(new BorderLayout());
        gui.add(pnlMiddle);

        Vector<String> headers = new Vector<>(List.of(new String[]{"ID", "Student ID", "Student name", "Module code", "Module name", ""}));
        Vector<Vector<?>> data = EnrollmentController.dtoIR;

        TableModel tm = new DefaultTableModel(data, headers);
        tblEnrollment = new MyJTable(tm);
        resetTableDimension();
        tblEnrollment.getTableHeader().setReorderingAllowed(false);
        ((MyJTable) tblEnrollment).setEditableColumns(true, 1, 2, 3, 4, 5);

        for (int i = 0; i < tm.getRowCount(); i++) {
            String sid = (String) tm.getValueAt(i, 1);
            Student student = studentDbContext.stream().filter(s -> s.getId().equals(sid)).findFirst().get();
            tm.setValueAt(student.getName(), i, 2);

            String mcode = (String) tm.getValueAt(i, 3);
            Module module = moduleDbContext.stream().filter(m -> m.getCode().equals(mcode)).findFirst().get();
            tm.setValueAt(module.getName(), i, 4);
        }
        TableColumn stuCol = tblEnrollment.getColumnModel().getColumn(1);
        stuCol.setCellEditor(new DefaultCellEditor(makeStudentComboBox()));

        TableColumn modCol = tblEnrollment.getColumnModel().getColumn(3);
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
        comboBox.addActionListener(e -> {
            TableModel tm = tblEnrollment.getModel();
            for (int i = 0; i < tm.getRowCount(); i++) {
                String sid = (String) tm.getValueAt(i, 1);
                Student student = studentDbContext.stream().filter(s -> s.getId().equals(sid)).findFirst().get();
                tm.setValueAt(student.getName(), i, 2);
            }
        });
        return comboBox;
    }

    private JComboBox<?> makeModuleComboBox() {
        String[] modules = moduleDbContext.stream().map(Module::getCode).toArray(String[]::new);
        JComboBox<?> comboBox = new JComboBox<>(modules);
        comboBox.setSelectedIndex(-1);
        comboBox.addActionListener(e -> {
            TableModel tm = tblEnrollment.getModel();
            for (int i = 0; i < tm.getRowCount(); i++) {
                String mcode = (String) tm.getValueAt(i, 3);
                Module module = moduleDbContext.stream().filter(m -> m.getCode().equals(mcode)).findFirst().get();
                tm.setValueAt(module.getName(), i, 4);
            }
        });
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
        tblEnrollment.getColumnModel().getColumn(5).setMaxWidth(70);
    }

    /**
     * this method notify the view that data in the logic model has been changed
     */
    @Override
    public void notifyDataChanged() {
        btnCheckAll.setText("  Check All  ");

        Vector<String> headers = new Vector<>(List.of(new String[]{"ID", "Student ID", "Student name", "Module code", "Module name", ""}));
        Vector<Vector<?>> data = EnrollmentController.dtoIR;
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        tblEnrollment.setModel(tm);
        resetTableDimension();

        TableColumn stuCol = tblEnrollment.getColumnModel().getColumn(1);
        stuCol.setCellEditor(new DefaultCellEditor(makeStudentComboBox()));

        TableColumn modCol = tblEnrollment.getColumnModel().getColumn(3);
        modCol.setCellEditor(new DefaultCellEditor(makeModuleComboBox()));
    }
}
