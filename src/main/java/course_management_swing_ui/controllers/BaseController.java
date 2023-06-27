package course_management_swing_ui.controllers;

import course_management_swing_ui.util.EnumUtil;
import course_management_swing_ui.views.View;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Phan Quang Tuan
 * @Overview An Interface represents the most basic controllers, which handles the user interaction of its views
 */
public abstract class BaseController extends WindowAdapter implements ActionListener {
    protected View view;
    public final static List<BaseController> controllers = new ArrayList<>();

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

    public static void resetOtherControllerViews(EnumUtil.Controller cause) {
        List<View> modifies;
        switch (cause) {
            case StudentController:
            case ModuleController:
                modifies = controllers.stream()
                        .filter(c -> {
                            if (c == null) return false;
                            String name = c.getClass().getSimpleName();
                            return Objects.equals(name, EnumUtil.Controller.EnrollmentController.toString());
                        })
                        .map(BaseController::getView)
                        .collect(Collectors.toList());
                break;
            case EnrollmentController:
                modifies = controllers.stream()
                        .filter(c -> {
                            if (c == null) return false;
                            String name = c.getClass().getSimpleName();
                            return Objects.equals(name, EnumUtil.Controller.StudentController.toString()) || Objects.equals(name, EnumUtil.Controller.ModuleController.toString());
                        })
                        .map(BaseController::getView)
                        .collect(Collectors.toList());
                break;
            default:
                return;
        }
        System.out.println(modifies);
        modifies.forEach(v -> {
            if (v == null) return;
            v.notifyDataChanged();
        });
    }
}
