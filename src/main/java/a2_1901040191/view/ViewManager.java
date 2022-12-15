package a2_1901040191.view;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview A class that contains viewMap to manage all view. This help us find the necessary view by its hashcode.
 * This class also provides methods for modify Look-and-Feel UI of Java Swing API. Moreover, other helper methods are also
 * stored here
 */
public abstract class ViewManager {
    public static final Map<Integer, View> viewMap = new HashMap<>();

    /**
     * @requires gui!=null
     * @modifies gui
     * @effects set gui's size to fit user screen
     */
    public static void setDefaultSize(JFrame gui) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        gui.setBounds(screenWidth / 4, screenHeight / 5, screenWidth / 2, screenWidth / 3);
    }

    /**
     * @requires v!=null
     * @modifies v
     * @effects set icon for the view v
     */
    public static void setupIcon(View v) {
        if (v != null && v.getGui() != null) {
            v.getGui().setIconImage(createImageIcon("src/main/resources/icon.png"));
        }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    public static Image createImageIcon(String path) {
        if (path != null) {
            return Toolkit.getDefaultToolkit().getImage(path);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Modify Look-and-Feel UI of Java Swing API.
     * reference from:
     * https://www.formdev.com/flatlaf/
     */
    public static void setUpDarkTheme() {
        FlatDarculaLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }
}
