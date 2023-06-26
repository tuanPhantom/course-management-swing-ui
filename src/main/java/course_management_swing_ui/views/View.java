package course_management_swing_ui.views;

import javax.swing.*;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the basic building block for user interface components. It's often contain a JFrame
 * and must contain methods to interact with that frame.
 */
public interface View {
    /**
     * This method is called when the views is first created. Similar to onCreate() in Android.
     */
    default void onCreate() {

    }

    /**
     * this method notify the views that data in the logic models has been changed
     */
    default void notifyDataChanged() {

    }

    /**
     * @efffects show <tt>gui</tt>
     */
    void display();

    void disposeGUI();

    /**
     * @effects dispose <tt>gui</tt>.
     */
    void shutDown();

    JFrame getGui();
}
