package a2_1901040191.controller;

import a2_1901040191.view.View;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

/**
 * @author Phan Quang Tuan
 * @Overview An Interface represents the most basic controller, which handles the user interaction of its view
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
     * @effects return view
     */
    public View getView() {
        return view;
    }
}
