package a2_1901040191.controller;

import a2_1901040191.model.Enrollment;
import a2_1901040191.model.Module;
import a2_1901040191.model.Student;
import a2_1901040191.repository.EnrollmentRepositoryImpl;
import a2_1901040191.repository.ModuleRepositoryImpl;
import a2_1901040191.repository.StudentRepositoryImpl;
import a2_1901040191.util.dto.DtoGenerator;
import a2_1901040191.view.View;
import a2_1901040191.view.ViewManager;
import a2_1901040191.view.enrollment.AssessmentReportView;
import a2_1901040191.view.enrollment.NewEnrollmentView;
import a2_1901040191.view.enrollment.InitialReportView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.*;

import static a2_1901040191.repository.DbContext.enrollmentDbContext;

/**
 * @author Phan Quang Tuan
 * @Overview Represents the controller component, which handles the user interaction as well as logic business of
 * Enrollment entities
 * @attributes <pre>
 *   View       View
 * </pre>
 * @Object AF(c) = { view }
 */
public class EnrollmentController extends BaseController {
    private final static StudentRepositoryImpl studentRepository = new StudentRepositoryImpl();
    private final static ModuleRepositoryImpl moduleRepository = new ModuleRepositoryImpl();
    private final static EnrollmentRepositoryImpl enrollmentRepository = new EnrollmentRepositoryImpl();
    public final static Vector<Vector<?>> dtoIR = new Vector<>();
    public final static Vector<Vector<?>> dtoAR = new Vector<>();

    public EnrollmentController() {
        super();
    }

    public EnrollmentController(View view) {
        super(view);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        view.disposeGUI();
    }

    /**
     * Handle events of NewEnrollmentView, InitialReportView, AssessmentReportView
     *
     * @effects <pre>
     * All event's action:
     * Case(s) of NewEnrollmentView
     *  Cancel:
     *      - close window
     *  Add:
     *      - find the student, module in DbContext
     *      - add new Enrollment to the DbContext
     *      - close window
     *
     * Case(s) of InitialReportView
     *  Add:
     *      - get the reference of NewEnrollmentView (init if null)
     *      - then display it
     *  Check All / Uncheck All:
     *      - toggle check button in InitialReportView / AssessmentReportView
     *      - check/uncheck all rows in InitialReportView.tblEnrollment / AssessmentReportView.tblEnrollment
     *  Delete:
     *      - delete all selected items
     *      - fetch new data
     *      - notify InitialReportView that data changed
     *      - uncheck all selected items
     *  Update:
     *      - get the ref of student & module in DbContext
     *      - get the ref of enrollment in DbContext
     *      - execute update by using enrollmentRepository
     *      - fetch new data
     *      - notify InitialReportView that data changed
     *      - uncheck all selected items
     *  Refresh Data
     *      - fetch new data
     *  Close
     *      - dispose view
     *  </pre>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (view instanceof NewEnrollmentView) {
            NewEnrollmentView v = (NewEnrollmentView) view;
            switch (command) {
                case "Cancel":
                    v.disposeGUI();
                    break;
                case "Add":
                    Student student = studentRepository.findById(Integer.valueOf(v.getSid().substring(1)));
                    Module module = moduleRepository.findById(v.getmCode());
                    addEnrollment(enrollmentDbContext.size() + 1, student, module, Double.parseDouble(v.getTxtIm().getText()), Double.parseDouble(v.getTxtEm().getText()));
                    v.disposeGUI();
                    break;
                default:
                    break;
            }
        } else if (view instanceof InitialReportView) {
            InitialReportView v = (InitialReportView) view;
            JFrame gui = v.getGui();
            JTable tblEnrollments = v.getTblEnrollment();
            switch (command) {
                case "Add":
                    View addGUI = NewEnrollmentView.getInstance();
                    if (addGUI == null) {
                        EnrollmentController ec = new EnrollmentController();
                        EnrollmentController.fetchData();
                        addGUI = NewEnrollmentView.getInstance(view.getGui(), ec);
                        ec.setGui(addGUI);
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
                        List<Enrollment> enrollments = new ArrayList<>();
                        DefaultTableModel tm = (DefaultTableModel) tblEnrollments.getModel();
                        for (int i = tblEnrollments.getRowCount() - 1; i >= 0; i--) {
                            boolean delete = (boolean) tm.getValueAt(i, 5);
                            if (delete) {
                                int id = (int) tm.getValueAt(i, 0);
                                enrollments.add(enrollmentRepository.findById(id));
                            }
                        }
                        if (enrollments.size() == enrollmentDbContext.size()) {
                            enrollmentRepository.deleteAll();
                        } else {
                            deleteEnrollment(enrollments);
                        }

                        fetchData();
                        // reset view in order to remove row(s)
                        v.notifyDataChanged();
                        setCheckAll(false);
                    }
                    break;
                case "Update":
                    // REMINDER:
                    //      When you want to create a new Enrollment object for updating,
                    //      which means neither resetEnrollmentIdCount() nor fetchData() are called before.
                    //      Please use the CONSTRUCTOR or equivalent methods THAT NOT MODIFY Db Context
                    //      which play an important role the process of generating the unique Enrollment.code.
                    //      If not, it will result in creating wrong id for the new Enrollment Object, which will replace the existing one.
                    // Solution:
                    //      use stream API or for-loop for finding the needed object in Enrollment Db Context,
                    //      then create new Enrollment object with the Constructor annotated with @Safe
                    int editResult = JOptionPane.showConfirmDialog(gui, "Update all the selected rows?", "Update confirmation", JOptionPane.OK_CANCEL_OPTION);
                    if (editResult == JOptionPane.YES_OPTION) {
                        List<Enrollment> enrollments = new ArrayList<>();
                        DefaultTableModel tm = (DefaultTableModel) tblEnrollments.getModel();
                        for (int i = tblEnrollments.getRowCount() - 1; i >= 0; i--) {
                            boolean edit = (boolean) tm.getValueAt(i, 5);
                            if (edit) {
                                int id = (int) tm.getValueAt(i, 0);
                                int sid = Integer.parseInt(((String) tm.getValueAt(i, 1)).substring(1));
                                String sName = (String) tm.getValueAt(i, 2);
                                String mCode = (String) tm.getValueAt(i, 3);
                                String mName = (String) tm.getValueAt(i, 4);
                                Enrollment enrollment = enrollmentRepository.findById(id);
                                double im = enrollment.getInternalMark(), em = enrollment.getExaminationMark();
                                Student student = studentRepository.findById(sid);
                                student.setName(sName);
                                studentRepository.update(student);
                                Module module = moduleRepository.findById(mCode);
                                module.setName(mName);
                                moduleRepository.update(module);
                                enrollment.setStudent(student);
                                enrollment.setModule(module);
                                enrollment.setInternalMark(im);
                                enrollment.setExaminationMark(em);
                                enrollments.add(enrollment);
                            }
                        }
                        updateEnrollment(enrollments);
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
        } else if (view instanceof AssessmentReportView) {
            AssessmentReportView v = (AssessmentReportView) view;
            JFrame gui = v.getGui();
            JTable tblEnrollments = v.getTblEnrollment();
            switch (command) {
                case "Add":
                    View addGUI = NewEnrollmentView.getInstance();
                    if (addGUI == null) {
                        EnrollmentController sc = new EnrollmentController();
                        EnrollmentController.fetchData();
                        addGUI = NewEnrollmentView.getInstance(view.getGui(), sc);
                        sc.setGui(addGUI);
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
                        List<Enrollment> enrollments = new ArrayList<>();
                        DefaultTableModel tm = (DefaultTableModel) tblEnrollments.getModel();
                        for (int i = tblEnrollments.getRowCount() - 1; i >= 0; i--) {
                            boolean delete = (boolean) tm.getValueAt(i, 6);
                            if (delete) {
                                int id = (int) tm.getValueAt(i, 0);
                                enrollments.add(enrollmentRepository.findById(id));
                            }
                        }
                        if (enrollments.size() == enrollmentDbContext.size()) {
                            enrollmentRepository.deleteAll();
                        } else {
                            deleteEnrollment(enrollments);
                        }

                        fetchData();
                        // reset view in order to remove row(s)
                        v.notifyDataChanged();
                        setCheckAll(false);
                    }
                    break;
                case "Update":
                    int editResult = JOptionPane.showConfirmDialog(gui, "Update all the selected rows?", "Update confirmation", JOptionPane.OK_CANCEL_OPTION);
                    if (editResult == JOptionPane.YES_OPTION) {
                        List<Enrollment> enrollments = new ArrayList<>();
                        DefaultTableModel tm = (DefaultTableModel) tblEnrollments.getModel();
                        for (int i = tblEnrollments.getRowCount() - 1; i >= 0; i--) {
                            boolean edit = (boolean) tm.getValueAt(i, 6);
                            if (edit) {
                                int id = (int) tm.getValueAt(i, 0);
                                int sid = Integer.parseInt(((String) tm.getValueAt(i, 1)).substring(1));
                                String mCode = (String) tm.getValueAt(i, 2);
                                Object imO = tm.getValueAt(i, 3), emO = tm.getValueAt(i, 4);
                                double im = imO instanceof String ? Double.parseDouble((String) imO) : (double) imO;
                                double em = emO instanceof String ? Double.parseDouble((String) emO) : (double) emO;
                                Enrollment enrollment = enrollmentRepository.findById(id);
                                Student student = studentRepository.findById(sid);
                                Module module = moduleRepository.findById(mCode);
                                enrollment.setStudent(student);
                                enrollment.setModule(module);
                                enrollment.setInternalMark(im);
                                enrollment.setExaminationMark(em);
                                enrollments.add(enrollment);
                            }
                        }
                        updateEnrollment(enrollments);
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
     * @modifies DbContext.enrollmentDbContext, ViewManager.viewMap
     * @effects <pre>
     *     - add to the database the new enrollment which made of id, student, module, im , em
     *     - fetch new data
     *     - notify the corresponding view in ViewManager.viewMap
     * </pre>
     */
    private void addEnrollment(int id, Student student, Module module, double im, double em) {
        if (Enrollment.validateId(id) && Enrollment.validateStudent(student) && Enrollment.validateModule(module) && Enrollment.validateMark(im) && Enrollment.validateMark(em)) {
            try {
                enrollmentRepository.add(new Enrollment(id, student, module, im, em));
                fetchData();
                Optional<View> opt1 = ViewManager.viewMap.values().stream().filter(m -> m instanceof InitialReportView).findFirst();
                Optional<View> opt2 = ViewManager.viewMap.values().stream().filter(m -> m instanceof AssessmentReportView).findFirst();
                if (opt1.isPresent()) {
                    InitialReportView v = (InitialReportView) opt1.get();
                    v.notifyDataChanged();
                }
                if (opt2.isPresent()) {
                    AssessmentReportView v = (AssessmentReportView) opt2.get();
                    v.notifyDataChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @effects execute deletion of all Enrollment in enrollments
     */
    private void deleteEnrollment(List<Enrollment> enrollments) {
        if (enrollments != null && enrollments.size() > 0) {
            enrollmentRepository.delete(enrollments);
        }
    }

    /**
     * @effects execute update of all Enrollment in enrollments
     */
    private void updateEnrollment(List<Enrollment> enrollments) {
        if (enrollments != null && enrollments.size() > 0) {
            enrollmentRepository.update(enrollments);
        }
    }

    /**
     * Get data from the DbContext. More specifically, get all enrollments and save it to enrollmentDbContext.
     *
     * @modifies DbContext
     * @effects <pre>
     *      Clear old data from DbContext (Student, Module, Enrollment)
     *      StudentController.fetchData(); -> get all students
     *      ModuleController.fetchData();  -> get all module
     *      Then process to add all the correspond Enrollment(s) to DbContext.enrollmentRepository
     *      Update new data to Dto(s)
     * </pre>
     */
    public static void fetchData() {
        System.out.println("----------------------------");
        enrollmentDbContext.clear();

        StudentController.fetchData();
        ModuleController.fetchData();
        enrollmentDbContext.addAll(enrollmentRepository.findAll());

        dtoIR.clear();
        dtoIR.addAll(DtoGenerator.getDto_initialReport(enrollmentDbContext));

        dtoAR.clear();
        dtoAR.addAll(DtoGenerator.getDto_assessmentReport(enrollmentDbContext));
        System.out.println("fetched new data from the database for: Enrollment");
        System.out.println("----------------------------");
    }

    /**
     * @effects set all rows in InitialReportView or AssessmentReportView with the declared value
     */
    private void setCheckAll(boolean value) {
        if (view instanceof InitialReportView) {
            InitialReportView v = (InitialReportView) view;
            JTable tblEnrollments = v.getTblEnrollment();
            JButton btn = v.btnCheckAll;
            for (int i = 0; i < tblEnrollments.getRowCount(); i++) {
                tblEnrollments.setValueAt(value, i, 5);
            }
            if (value) {
                btn.setText("Uncheck All");
            } else {
                btn.setText("  Check All  ");
            }
        } else {
            AssessmentReportView v = (AssessmentReportView) view;
            JTable tblEnrollments = v.getTblEnrollment();
            JButton btn = v.btnCheckAll;
            for (int i = 0; i < tblEnrollments.getRowCount(); i++) {
                tblEnrollments.setValueAt(value, i, 6);
            }
            if (value) {
                btn.setText("Uncheck All");
            } else {
                btn.setText("  Check All  ");
            }
        }
    }
}
