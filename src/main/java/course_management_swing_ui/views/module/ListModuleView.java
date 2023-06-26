package course_management_swing_ui.views.module;

import course_management_swing_ui.controllers.ModuleController;
import course_management_swing_ui.models.Module;
import course_management_swing_ui.util.MyJTable;
import course_management_swing_ui.views.View;
import course_management_swing_ui.views.ViewManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the views for "List module". It is also a singleton class to prevent JFrame
 * window spamming
 */
public class ListModuleView implements View {
    private static ListModuleView instance;
    private ModuleController ctrl;

    private JFrame gui;
    private JFrame parentGui;
    private JTable tblModules;
    public JButton btnCheckAll;

    private ListModuleView() {

    }

    public static ListModuleView getInstance() {
        return instance;
    }

    public static ListModuleView getInstance(ModuleController ctrl, JFrame parentGui) {
        if (instance == null) {
            instance = new ListModuleView();
            instance.ctrl = ctrl;
            instance.parentGui = parentGui;
            instance.onCreate();
        }
        return instance;
    }

    /**
     * This method is called when the views is first created. Similar to onCreate() in Android.
     */
    @Override
    public void onCreate() {
        gui = new JFrame("List modules");
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
        JLabel lblTitle = new JLabel("List of modules");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 15f));
        pnlTop.add(lblTitle);
        gui.add(pnlTop, BorderLayout.NORTH);

        // center panel
        JPanel pnlMiddle = new JPanel();
        pnlMiddle.setLayout(new BorderLayout());
        gui.add(pnlMiddle);

        Vector<String> headers = new Vector<>(List.of(new String[]{"Code", "Name", "Semester", "Credits", "Module type", "Department", ""}));
        Vector<Vector<?>> data = ModuleController.dto;

        TableModel tm = new DefaultTableModel(data, headers);
        tblModules = new MyJTable(tm);
        resetTableDimension();
        tblModules.getTableHeader().setReorderingAllowed(false);
        ((MyJTable) tblModules).setEditableColumns(true, 1, 3, 4, 6);
        for (int i = 0; i < tm.getRowCount(); i++) {
            ((MyJTable) tblModules).setEditableCell(tm.getValueAt(i, 4) == Module.ModuleType.ELECTIVE, i, 5);
        }
        TableColumn comboBoxColumn = tblModules.getColumnModel().getColumn(4);
        comboBoxColumn.setCellEditor(new DefaultCellEditor(makeModuleListViewComboBox()));

        JScrollPane scrContacts = new JScrollPane(tblModules);
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

    private JComboBox<?> makeModuleListViewComboBox() {
        Module.ModuleType[] moduleTypes = {Module.ModuleType.COMPULSORY, Module.ModuleType.ELECTIVE};
        JComboBox<?> comboBox = new JComboBox<>(moduleTypes);
        comboBox.setSelectedIndex(-1);
        comboBox.addActionListener(e -> {
            TableModel tm = tblModules.getModel();
            for (int i = 0; i < tm.getRowCount(); i++) {
                ((MyJTable) tblModules).setEditableCell(tm.getValueAt(i, 4) == Module.ModuleType.ELECTIVE, i, 5);
                if (tm.getValueAt(i, 4) != Module.ModuleType.ELECTIVE) {
                    tm.setValueAt("", i, 5);
                }
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
     * @effects return tblModules
     */
    public JTable getTblModules() {
        return tblModules;
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
        tblModules.setRowHeight(35);
        tblModules.getColumnModel().getColumn(6).setMaxWidth(70);
    }

    /**
     * this method notify the views that data in the logic models has been changed
     */
    @Override
    public void notifyDataChanged() {
        btnCheckAll.setText("  Check All  ");

        Vector<String> headers = new Vector<>(List.of(new String[]{"Code", "Name", "Semester", "Credits", "Module type", "Department", ""}));
        Vector<Vector<?>> data = ModuleController.dto;
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        tblModules.setModel(tm);
        resetTableDimension();

        for (int i = 0; i < tm.getRowCount(); i++) {
            ((MyJTable) tblModules).setEditableCell(tm.getValueAt(i, 4) == Module.ModuleType.ELECTIVE, i, 5);
        }
        TableColumn comboBoxColumn = tblModules.getColumnModel().getColumn(4);
        comboBoxColumn.setCellEditor(new DefaultCellEditor(makeModuleListViewComboBox()));
    }
}
