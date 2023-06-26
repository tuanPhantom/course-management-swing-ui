package course_management_swing_ui.controllers;

import course_management_swing_ui.factories.ModuleFactory;
import course_management_swing_ui.models.CompulsoryModule;
import course_management_swing_ui.models.ElectiveModule;
import course_management_swing_ui.models.Module;
import course_management_swing_ui.services.ModuleService;
import course_management_swing_ui.util.dto.DtoGenerator;
import course_management_swing_ui.util.exceptions.NotPossibleException;
import course_management_swing_ui.views.View;
import course_management_swing_ui.views.ViewManager;
import course_management_swing_ui.views.module.NewModuleView;
import course_management_swing_ui.views.module.ListModuleView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.*;

import static course_management_swing_ui.repositories.DbContext.moduleDbContext;

/**
 * @author Phan Quang Tuan
 * @Overview Represents the controllers component, which handles the user interaction as well as logic business of Module
 * entities
 * @attributes <pre>
 *   View       View
 * </pre>
 * @Object AF(c) = { views }
 */
public class ModuleController extends BaseController {
    private final static ModuleService moduleService = new ModuleService();
    public final static Vector<Vector<?>> dto = new Vector<>();

    public ModuleController() {
        super();
    }

    public ModuleController(View view) {
        super(view);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        view.disposeGUI();
    }

    /**
     * Handle events of NewModuleView, ListModuleView
     *
     * @effects <pre>
     * All event's action:
     * Case(s) of NewModuleView
     *  Cancel:
     *      - close window
     *  Add:
     *      - add new Module to DbContext
     *      - close window
     *
     * Case(s) of ListModuleView
     *  Add:
     *      - get the reference of NewModuleView (init if null)
     *      - then display it
     *  Check All / Uncheck All:
     *      - toggle check button in ListModuleView
     *      - check/uncheck all rows in ListModuleView.tblModules
     *  Delete:
     *      - delete all selected items
     *      - fetch new data
     *      - notify ListModuleView that data changed
     *      - uncheck all selected items
     *  Update:
     *      - get the ref of module in DbContext
     *      - execute update by using moduleRepository
     *      - fetch new data
     *      - notify ListModuleView that data changed
     *      - uncheck all selected items
     *  Refresh Data
     *      - fetch new data
     *  Close
     *      - dispose views
     *  </pre>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (view instanceof NewModuleView) {
            NewModuleView v = (NewModuleView) view;
            switch (command) {
                case "Cancel":
                    v.disposeGUI();
                    break;
                case "Add":
                    try {
                        if (addModule(v.getTxtName().getText(), Integer.parseInt(v.getTxtSemester().getText()), Integer.parseInt(v.getTxtCredits().getText()), v.getModuleType(), v.getTxtDepartment().getText())) {
                            v.disposeGUI();
                        }
                    } catch (Exception ex) {
                        System.out.println("Invalid input for new Module!");
                        JOptionPane.showMessageDialog(view.getGui(), "Invalid input for new Module!", "Failed To add new Module", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                default:
                    break;
            }
        } else if (view instanceof ListModuleView) {
            ListModuleView v = (ListModuleView) view;
            JFrame gui = v.getGui();
            JTable tblModules = v.getTblModules();
            switch (command) {
                case "Add":
                    View addGUI = NewModuleView.getInstance();
                    if (addGUI == null) {
                        ModuleController mc = new ModuleController();
                        ModuleController.fetchData();
                        addGUI = NewModuleView.getInstance(view.getGui(), mc);
                        mc.setGui(addGUI);
                        ViewManager.viewMap.put(addGUI.hashCode(), addGUI);
                    }
                    addGUI.display();
                    break;
                case "  Check All  ":
                    setCheckAll(true);
                    //JButton btn = (JButton) e.getSource();
                    break;
                case "Uncheck All":
                    setCheckAll(false);
                    break;
                case "Delete":
                    int result = JOptionPane.showConfirmDialog(gui, "Are you sure?", "Delete confirmation", JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        List<Module> modules = new ArrayList<>();
                        DefaultTableModel tm = (DefaultTableModel) tblModules.getModel();
                        for (int i = tblModules.getRowCount() - 1; i >= 0; i--) {
                            boolean delete = (boolean) tm.getValueAt(i, 6);
                            if (delete) {
                                String code = String.valueOf(tm.getValueAt(i, 0));
                                modules.add(moduleService.findById(code));
                            }
                        }
                        if (modules.size() == moduleDbContext.size()) {
                            moduleService.deleteAll();
                        } else {
                            deleteModule(modules);
                        }

                        fetchData();
                        // reset views in order to remove row(s)
                        v.notifyDataChanged();
                        setCheckAll(false);
                    }
                    break;
                case "Update":
                    // REMINDER:
                    //      When you want to create a new Module object for updating,
                    //      which means neither resetModuleIdCount() nor fetchData() are called before.
                    //      Please use the CONSTRUCTOR or equivalent methods THAT NOT MODIFY the Module.suffixes,
                    //      which play an important role the process of generating the unique Module.code.
                    //      If not, it will result in creating wrong id for the new Module Object, which will replace the existing one.
                    // Solution:
                    //      use stream API or for-loop for finding the needed object in Module Db Context,
                    //      then create new Module object with the Constructor annotated with @Safe
                    int editResult = JOptionPane.showConfirmDialog(gui, "Update all the selected rows?", "Update confirmation", JOptionPane.OK_CANCEL_OPTION);
                    if (editResult == JOptionPane.YES_OPTION) {
                        List<Module> modules = new ArrayList<>();
                        DefaultTableModel tm = (DefaultTableModel) tblModules.getModel();
                        for (int i = tblModules.getRowCount() - 1; i >= 0; i--) {
                            boolean edit = (boolean) tm.getValueAt(i, 6);
                            if (edit) {
                                String code = (String) tm.getValueAt(i, 0);
                                String name = (String) tm.getValueAt(i, 1);
                                int semester = (int) tm.getValueAt(i, 2);
                                Object creditObject = tm.getValueAt(i, 3);
                                int credits = creditObject instanceof Integer ? (int) creditObject : Integer.parseInt((String) creditObject);
                                Module.ModuleType mt = (Module.ModuleType) tm.getValueAt(i, 4);
                                moduleDbContext.removeIf(m -> m.getCode().equals(code)); // remove here because module can be switched between two types

                                if (mt == Module.ModuleType.ELECTIVE) {
                                    String department = (String) tm.getValueAt(i, 5);
                                    try {
                                        // not generating Module.code -> it's safe
                                        Module m = new ElectiveModule(code, name, semester, credits, department);
                                        modules.add(m);
                                    } catch (NotPossibleException ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Module m = new CompulsoryModule(code, name, semester, credits);
                                        modules.add(m);
                                    } catch (NotPossibleException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                        updateModule(modules);
                        fetchData();
                        v.notifyDataChanged();
                        setCheckAll(false);
                    }
                    break;
                case "Refresh Data":
                    fetchData();
                    v.notifyDataChanged();
                    setCheckAll(false);
                    break;

                case "Close":
                    view.shutDown();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @modifies DbContext.moduleDbContext, ViewManager.viewMap
     * @effects <pre>
     *     - add to the database the new Modulee which made of name, semester, credits, mt , department
     *     - fetch new data
     *     - notify the corresponding views in ViewManager.viewMap
     * </pre>
     */
    private boolean addModule(String name, int semester, int credits, Module.ModuleType mt, String department) {
        if (Module.validateName(name) && Module.validateSemester(semester) && Module.validateCredits(credits) && Module.validateModuleType(mt)) {
            try {
                moduleService.add(ModuleFactory.getInstance().createModule(name, semester, credits, mt, department));
                fetchData();
                Optional<View> opt = ViewManager.viewMap.values().stream().filter(m -> m instanceof ListModuleView).findFirst();
                if (opt.isPresent()) {
                    ListModuleView v = (ListModuleView) opt.get();
                    v.notifyDataChanged();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid input for new Module!");
            JOptionPane.showMessageDialog(view.getGui(), "Invalid input for new Module!", "Failed To add new Module", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * @effects execute deletion of all Module in modules
     */
    private void deleteModule(List<Module> modules) {
        if (modules != null && modules.size() > 0) {
            moduleService.delete(modules);
        }
    }


    /**
     * @effects execute update of all Module in modules
     */
    private void updateModule(List<Module> modules) {
        if (modules != null && modules.size() > 0) {
            moduleService.update(modules);
        }
    }

    /**
     * Get data from the DbContext. More specifically, get all Module and save it to moduleDbContext.
     *
     * @modifies DbContext.moduleDbContext
     * @effects <pre>
     *      Clear old data from DbContext (Module only)
     *      Then process to add all the correspond Module(s) to DbContext.moduleDbContext
     *      Update new data to this.dto
     * </pre>
     */
    public static void fetchData() {
        resetModuleIdCount();
        moduleDbContext.clear();
        moduleDbContext.addAll(moduleService.findAll());

        dto.clear();
        dto.addAll(DtoGenerator.getDto_module(moduleDbContext));
        System.out.println("fetched new data from the database for: Module");
    }

    /**
     * @effects delete all suffixes stored in the static HashMap called Module.suffixes
     */
    private static void resetModuleIdCount() {
        Module.suffixes.clear();
    }

    /**
     * @effects set all rows in ListModuleView with the declared value
     */
    private void setCheckAll(boolean value) {
        ListModuleView v = (ListModuleView) view;
        JTable tblModules = v.getTblModules();
        JButton btn = v.btnCheckAll;
        for (int i = 0; i < tblModules.getRowCount(); i++) {
            tblModules.setValueAt(value, i, 6);
        }
        if (value) {
            btn.setText("Uncheck All");
        } else {
            btn.setText("  Check All  ");
        }
    }
}
