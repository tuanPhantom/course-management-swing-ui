package course_management_swing_ui.view.student;

import course_management_swing_ui.controller.StudentController;
import course_management_swing_ui.view.View;
import course_management_swing_ui.view.ViewManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the view for "List student". It is also a singleton class to prevent JFrame
 * window spamming
 */
public class ListStudentView implements View {
    private static ListStudentView instance;
    private StudentController ctrl;

    private JFrame gui;
    private JFrame parentGui;
    private JTable tblStudents;
    public JButton btnCheckAll;

    private ListStudentView() {

    }

    public static ListStudentView getInstance() {
        return instance;
    }

    public static ListStudentView getInstance(StudentController ctrl, JFrame parentGui) {
        if (instance == null) {
            instance = new ListStudentView();
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
        gui = new JFrame("List students");
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
        JLabel lblTitle = new JLabel("List of students");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 15f));
        pnlTop.add(lblTitle);
        gui.add(pnlTop, BorderLayout.NORTH);

        // center panel
        JPanel pnlMiddle = new JPanel();
        pnlMiddle.setLayout(new BorderLayout());
        gui.add(pnlMiddle);

        Vector<String> headers = new Vector<>(List.of(new String[]{"ID", "Name", "Date of Birth", "address", "email", ""}));
        Vector<Vector<?>> data = StudentController.dto;

        DefaultTableModel tm = new DefaultTableModel(data, headers);
        tblStudents = new JTable(tm) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 5) {
                    return Boolean.class;
                } else {
                    return super.getColumnClass(column);
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        resetTableDimension();
        tblStudents.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrContacts = new JScrollPane(tblStudents);
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

    /**
     * @effects return gui
     */
    @Override
    public JFrame getGui() {
        return gui;
    }

    /**
     * @effects return tblStudent
     */
    public JTable getTblStudents() {
        return tblStudents;
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
        tblStudents.setRowHeight(35);
        tblStudents.getColumnModel().getColumn(5).setMaxWidth(70);
    }

    /**
     * this method notify the view that data in the logic model has been changed
     */
    @Override
    public void notifyDataChanged() {
        btnCheckAll.setText("  Check All  ");

        Vector<String> headers = new Vector<>(List.of(new String[]{"ID", "Name", "Date of Birth", "address", "email", ""}));
        Vector<Vector<?>> data = StudentController.dto;
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        tblStudents.setModel(tm);
        resetTableDimension();
    }
}
