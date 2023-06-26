package course_management_swing_ui;

import course_management_swing_ui.controller.MainController;
import course_management_swing_ui.view.ViewManager;
import course_management_swing_ui.view.MainWindowView;

/**
 * Requires jdk >= 11
 * @overview
 *  Represents a standard desktop GUI
 *
 * @author Phan Quang Tuan
 */
public class CourseManProg {
    private MainController c;

    private MainWindowView g;

    /**
     * @effects
     *  initialise <tt>c</tt> and invoke <tt>c.displayGUI()</tt>
     */
    public CourseManProg() {
        c = new MainController();
        g = MainWindowView.getInstance(c);
        ViewManager.viewMap.put(g.hashCode(), g);
        c.setGui(g);
    }

    /**
     * @effects show window <tt>g</tt>
     */
    public void display() {
        g.display();
        // set window size and location
        ViewManager.setDefaultSize(g.getGui());
    }

    /**
     * Requires jdk >= 11
     * The run method
     * @effects
     *  create an instance of <tt>CourseManProg</tt>
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            ViewManager.setUpDarkTheme();
            CourseManProg app = new CourseManProg();
            app.display();
        });

    }
}
