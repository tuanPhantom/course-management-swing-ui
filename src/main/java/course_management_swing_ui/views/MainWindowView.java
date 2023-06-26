package course_management_swing_ui.views;

import course_management_swing_ui.controllers.MainController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This class represents the views when the program begins. It is also a singleton class to prevent JFrame
 * window spamming
 */
public class MainWindowView implements View {
    private static MainWindowView instance;
    private MainController ctrl;
    private JFrame gui;
    private JPanel centre;

    private MainWindowView() {

    }

    public static MainWindowView getInstance() {
        return instance;
    }

    /**
     * @effects initialise <tt>gui</tt> and the display components
     */
    public static MainWindowView getInstance(MainController ctrl) {
        if (instance == null) {
            instance = new MainWindowView();
            instance.ctrl = ctrl;
            instance.onCreate();
        }
        return instance;
    }

    /**
     * This method is called when the views is first created. Similar to onCreate() in Android.
     * @effects initialise <tt>gui</tt> with a menu bar; initialise the display components and add them to <tt>gui</tt>
     */
    @Override
    public void onCreate() {
        // the window
        gui = new JFrame("CourseManProg");
        gui.addWindowListener(ctrl);
        ViewManager.setupIcon(this);

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(ctrl);
        file.add(exit);

        JMenu studentMenu = new JMenu("Student");
        JMenuItem newStudent = new JMenuItem("New student");
        newStudent.addActionListener(ctrl);
        JMenuItem listStudent = new JMenuItem("List students");
        listStudent.addActionListener(ctrl);
        studentMenu.add(newStudent);
        studentMenu.add(listStudent);

        JMenu moduleMenu = new JMenu("Module");
        JMenuItem newModule = new JMenuItem("New module");
        newModule.addActionListener(ctrl);
        JMenuItem listModules = new JMenuItem("List modules");
        listModules.addActionListener(ctrl);
        moduleMenu.add(newModule);
        moduleMenu.add(listModules);

        JMenu enrolmentMenu = new JMenu("Enrolment");
        JMenuItem newEnrollment = new JMenuItem("New enrollment");
        newEnrollment.addActionListener(ctrl);
        JMenuItem initialReport = new JMenuItem("Initial report");
        initialReport.addActionListener(ctrl);
        JMenuItem assessmentReport = new JMenuItem("Assessment report");
        assessmentReport.addActionListener(ctrl);
        enrolmentMenu.add(newEnrollment);
        enrolmentMenu.add(initialReport);
        enrolmentMenu.add(assessmentReport);

        menuBar.add(file);
        menuBar.add(studentMenu);
        menuBar.add(moduleMenu);
        menuBar.add(enrolmentMenu);

        gui.setJMenuBar(menuBar);

        // the panels
        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gui.add(north, BorderLayout.NORTH);
        gui.add(north, BorderLayout.CENTER);

        // centre panel
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/main/resources/mainwindow_bg.jpg"));
        } catch (IOException e) {
            System.out.println("Cannot find the image's path");
        }
        BufferedImage finalImage = image;
        centre = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(finalImage, 0, 0, centre.getWidth(), centre.getHeight(), null);
            }
        };
        gui.add(centre, BorderLayout.CENTER);

        // set up window
        gui.pack();
    }

    /**
     * @effects return gui
     */
    @Override
    public JFrame getGui() {
        return gui;
    }

    @Override
    public void display() {
        if (!gui.isVisible())
            gui.setVisible(true);
    }

    @Override
    public void disposeGUI() {
        gui.dispose();
    }

    @Override
    public void shutDown() {
        gui.dispose();
    }
}
