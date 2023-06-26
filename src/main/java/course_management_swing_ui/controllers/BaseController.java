package course_management_swing_ui.controllers;

import course_management_swing_ui.views.View;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

/**
 * @author Phan Quang Tuan
 * @Overview An Interface represents the most basic controllers, which handles the user interaction of its views
 */
public abstract class BaseController extends WindowAdapter implements ActionListener {
    protected View view;

    public BaseController() {

    }

    public BaseController(View gui) {
        setGui(gui);
    }

    /**
     * @effects sets <tt>this.gui = gui</tt>
     */
    public void setGui(View gui) {
        this.view = gui;
    }

    /**
     * @effects return views
     */
    public View getView() {
        return view;
    }
}
