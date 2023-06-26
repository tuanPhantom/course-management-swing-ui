package course_management_swing_ui.factories;

import course_management_swing_ui.models.CompulsoryModule;
import course_management_swing_ui.models.ElectiveModule;
import course_management_swing_ui.models.Module;
import course_management_swing_ui.util.Safe;
import course_management_swing_ui.util.exceptions.InvalidArgumentException;
import course_management_swing_ui.util.exceptions.NotPossibleException;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview A class implements factories pattern, A kind of creational design pattern that provides an interface for
 * creating Module objects in a superclass, but allows subclasses to alter the type of objects that will be created,
 * which is either COMPULSORY or ELECTIVE
 */
public class ModuleFactory {
    private static ModuleFactory instance;

    private ModuleFactory() {
    }

    public static ModuleFactory getInstance() {
        if (instance == null) {
            instance = new ModuleFactory();
        }
        return instance;
    }

    /**
     * @effects create new Module and calculate its code
     */
    public Module createModule(String name, int semester, int credits) throws NotPossibleException {
        return new CompulsoryModule(name, semester, credits);
    }

    /**
     * @effects create new Module and calculate its code
     */
    public Module createModule(String name, int semester, int credits, Module.ModuleType moduleType, String department) throws NotPossibleException, InvalidArgumentException {
        switch (moduleType) {
            case COMPULSORY:
                return createModule(name, semester, credits);
            case ELECTIVE:
                return new ElectiveModule(name, semester, credits, department);
            default:
                throw new InvalidArgumentException("Unsupported module type!");
        }
    }

    /**
     * @requires code is fetched from the database
     * @effects create a new Module using code that is directly fetched rather than calculated.
     */
    @Safe
    public Module createModule(String code, String name, int semester, int credits) throws NotPossibleException {
        return new CompulsoryModule(code, name, semester, credits);
    }

    /**
     * @requires code is fetched from the database
     * @effects create a new Module using code that is directly fetched rather than calculated.
     */
    @Safe
    public Module createModule(String code, String name, int semester, int credits, Module.ModuleType moduleType, String department) throws NotPossibleException, InvalidArgumentException {
        switch (moduleType) {
            case COMPULSORY:
                return createModule(code, name, semester, credits);
            case ELECTIVE:
                return new ElectiveModule(code, name, semester, credits, department);
            default:
                throw new InvalidArgumentException("Unsupported module type!");
        }
    }
}
