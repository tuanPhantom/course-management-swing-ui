package course_management_swing_ui.controllers;

import course_management_swing_ui.util.ThreadPool;
import course_management_swing_ui.views.ViewManager;
import course_management_swing_ui.views.enrollment.AssessmentReportView;
import course_management_swing_ui.views.enrollment.InitialReportView;
import course_management_swing_ui.views.enrollment.NewEnrollmentView;
import course_management_swing_ui.views.module.NewModuleView;
import course_management_swing_ui.views.module.ListModuleView;
import course_management_swing_ui.views.student.NewStudentView;
import course_management_swing_ui.views.student.ListStudentView;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

/**
 * @author Phan Quang Tuan
 * @Overview Represents the controllers of MainWindowView
 * @attributes <pre>
 *   View       View
 * </pre>
 * @Object AF(c) = { views }
 */
public class MainController extends BaseController {

    /**
     * @effects <pre>
     * Handle the following cases:
     * File
     *    Exit – terminates the program.
     * Student
     *    New student – This menu item opens a window for user to add a new student.
     *    List students – This menu item opens a window which displays a table of students.
     * Module
     *    New module – This menu item opens a window for user to add a new module.
     *    List modules – This menu item opens a window which displays a table of modules.
     * Enrolment
     *    New enrolment – This menu item opens a window for user to enrol a Student into a Module. There should be two drop-down boxes for selecting Student and Module respectively.
     *    Initial report – This menu item opens a window which displays a tabular list of enrolments without grades.
     *    Assessment report – This menu item opens a window which displays a tabular list of enrolments with grades.
     *
     * </pre>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "Exit":
                shutDown();
                break;
            case "New student":
                NewStudentView addGui = NewStudentView.getInstance();
                if (addGui == null) {
                    StudentController studentCtrl = new StudentController();
                    StudentController.fetchData();
                    addGui = NewStudentView.getInstance(view.getGui(), studentCtrl);
                    studentCtrl.setGui(addGui);
                    ViewManager.viewMap.put(addGui.hashCode(), addGui);
                }
                addGui.display();
                break;
            case "List students":
                ListStudentView listGui = ListStudentView.getInstance();
                if (listGui == null) {
                    StudentController listStudentCtrl = new StudentController();
                    StudentController.fetchData();
                    listGui = ListStudentView.getInstance(listStudentCtrl, view.getGui());
                    listStudentCtrl.setGui(listGui);
                    ViewManager.viewMap.put(listGui.hashCode(), listGui);
                } else {
                    StudentController.fetchData();
                    listGui.notifyDataChanged();
                }
                listGui.display();
                break;
            case "New module":
                NewModuleView amv = NewModuleView.getInstance();
                if (amv == null) {
                    ModuleController moduleCtrl = new ModuleController();
                    ModuleController.fetchData();
                    amv = NewModuleView.getInstance(view.getGui(), moduleCtrl);
                    moduleCtrl.setGui(amv);
                    ViewManager.viewMap.put(amv.hashCode(), amv);
                }
                amv.display();
                break;
            case "List modules":
                ListModuleView lmv = ListModuleView.getInstance();
                if (lmv == null) {
                    ModuleController moduleCtrl = new ModuleController();
                    ModuleController.fetchData();
                    lmv = ListModuleView.getInstance(moduleCtrl, view.getGui());
                    moduleCtrl.setGui(lmv);
                    ViewManager.viewMap.put(lmv.hashCode(), lmv);
                } else {
                    ModuleController.fetchData();
                    lmv.notifyDataChanged();
                }
                lmv.display();
                break;
            case "New enrollment":
                NewEnrollmentView emv = NewEnrollmentView.getInstance();
                if (emv == null) {
                    EnrollmentController emvCtrl = new EnrollmentController();
                    EnrollmentController.fetchData();
                    emv = NewEnrollmentView.getInstance(view.getGui(), emvCtrl);
                    emvCtrl.setGui(emv);
                    ViewManager.viewMap.put(emv.hashCode(), emv);
                }
                emv.display();
                break;
            case "Initial report":
                InitialReportView irv = InitialReportView.getInstance();
                if (irv == null) {
                    EnrollmentController irvCtrl = new EnrollmentController();
                    EnrollmentController.fetchData();
                    irv = InitialReportView.getInstance(irvCtrl, view.getGui());
                    irvCtrl.setGui(irv);
                    ViewManager.viewMap.put(irv.hashCode(), irv);
                } else {
                    EnrollmentController.fetchData();
                    irv.notifyDataChanged();
                }
                irv.display();
                break;
            case "Assessment report":
                AssessmentReportView arv = AssessmentReportView.getInstance();
                if (arv == null) {
                    EnrollmentController arvCtrl = new EnrollmentController();
                    EnrollmentController.fetchData();
                    arv = AssessmentReportView.getInstance(arvCtrl, view.getGui());
                    arvCtrl.setGui(arv);
                    ViewManager.viewMap.put(arv.hashCode(), arv);
                } else {
                    EnrollmentController.fetchData();
                    arv.notifyDataChanged();
                }
                arv.display();
                break;
            default:
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        shutDown();
    }

    /**
     * Shut down this application.
     *
     * @effects shutdown <tt>gui</tt> and exit.
     */
    private void shutDown() {
        view.shutDown();

        // turn off services
        ThreadPool.executor.shutdown();

        // exit program
        System.exit(0);
    }
}
