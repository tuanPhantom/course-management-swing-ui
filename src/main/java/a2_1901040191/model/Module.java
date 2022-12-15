package a2_1901040191.model;

import a2_1901040191.util.Safe;
import a2_1901040191.util.dto.*;
import a2_1901040191.util.exceptions.NotPossibleException;

import java.util.*;

/**
 * @Overview A class represents a unit that can form part of a course of study, especially at a college or university.
 * @attributes <pre>
 *  code            String
 *  name            String
 *  semester        String
 *  credits         int
 *  moduleType      ModuleType
 * </pre>
 * @Object a typical Module is M:<code, name, credits, semester>
 * AF(c) = < <"M" + (semester * 100 + idCount)> , name, semester, credits, moduleType>
 * @rep_invariant
 *  code != null /\ code.matches("^M[1-9]+[0-9]+[1-9]+$|^M[1-9]+[0-9]+[1-9]+[0-9]+$|^M[1-9]{2,}[0-9]+$") /\
 *  name != null /\
 *  semester >= 1 /\
 *  credits >= 0 /\
 *  moduleType != null
 * @version 1.0
 * @author Phan Quang Tuan
 */
public abstract class Module implements Cloneable {
    @DisplayInTable
    private String code;
    @DisplayInTable
    private String name;
    @DisplayInTable
    private int semester;
    @DisplayInTable
    private int credits;
    @DisplayInTable
    private ModuleType moduleType;

    // key = suffix formed by the semester and the number of modules sharing that same semester.
    // value = dummy object (to guarantee the key is unique)
    public final static HashMap<String, Object> suffixes = new HashMap<>();
    private final static String PREFIX = "M";

    /**
     * @modifies idCountMap
     * @effects <pre>
     * if rep_invariant are satisfied
     * 	initialize this as < <"M" + semester * 100 + idCount> , name, semester, credits, moduleType>
     * else
     * 	throw NotPossibleException</pre>
     */
    public Module(String name, int semester, int credits, ModuleType moduleType) throws NotPossibleException {
        if (!validateName(name)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid name: \"" + name + "\"");
        }

        if (!validateSemester(semester)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid semester: \"" + semester + "\"");
        }

        if (!validateCredits(credits)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid credits: \"" + credits + "\"");
        }

        if (!validateModuleType(moduleType)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid moduleType: \"" + moduleType + "\"");
        }

        this.name = name;
        this.semester = semester;
        this.moduleType = moduleType;
        this.credits = credits;
        this.code = calculateCode();
    }


    /**
     * Return a new String that matches the rep_invariant of `this.code`
     * @requires semester is validated
     * @modifies suffixes
     * @effects <pre>
     *  Start with: suffix = semester + String.of(0 and code | String.length() >= 2)
     *  check whether suffix is duplicated in suffixes
     *      while duplicated:
     *          increase code by 1, then re-check suffix
     *
     *  put the unique suffix to suffixes
     *  return this.PREFIX + suffix
     * </pre>
     *
     * @Gherkin-style-specification
     *  Feature: Creating new module
     *      Scenario: Calculating code as unique id of the new module
     *          Given module with the semester attribute value equal or greater than 1. (For instance, semester = 3)
     *          When there is no module in the same semester
     *          Then the output should be M301
     *          When there are total N=101 module in the same semester (counted this new module)
     *          Then the output should be M3101     (semester 3, N=101)
     *
     *          Given module with the semester attribute value equal or greater than 1. (For instance, semester = 31)
     *          When there is no module in the same semester /\ there is an existing semester with the code: M3101 (semester 3, N=101)
     *          Then the output should be M3102     (semester 31, N=1)
     */
    private String calculateCode() {
        int i = 1;
        String suffix = null;
        Object o = new Object();

        while (o != null) {
            suffix = semester + String.format("%02d", i);
            o = suffixes.get(suffix);
            i += 1;
        }

        // coming out from the loop means that the suffix is unique now
        // Then put it into this.suffixes
        suffixes.put(suffix, new Object());
        return PREFIX + suffix;
    }

    /**
     * @requires this.code was fetched from the database /\ suffixes.get(code[1,len])!=null
     * @effects <pre>
     * if rep_invariant are satisfied
     * 	initialize this as < code, name, semester, credits, moduleType >
     * else
     * 	throw NotPossibleException</pre>
     */
    @Safe
    public Module(String code, String name, int semester, int credits, ModuleType moduleType) throws NotPossibleException {
        if (!validateName(name)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid name: \"" + name + "\"");
        }

        if (!validateSemester(semester)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid semester: \"" + semester + "\"");
        }

        if (!validateCredits(credits)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid credits: \"" + credits + "\"");
        }

        if (!validateModuleType(moduleType)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid moduleType: \"" + moduleType + "\"");
        }

        if (suffixes.get(code.substring(1)) != null) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid code: \"" + code + "\"");
        }

        this.code = code;
        this.name = name;
        this.semester = semester;
        this.moduleType = moduleType;
        this.credits = credits;

        suffixes.put(code.substring(1), new Object());
    }

    /**
     * @effects return code
     */
    public String getCode() {
        return code;
    }

    // case: code is in { M1001 , M10101 }
    private final static String regex01 = "^M[1-9]+[0-9]+[1-9]+$";

    // case: code is in { M1010 , M10100 }
    private final static String regex02 = "^M[1-9]+[0-9]+[1-9]+[0-9]+$";

    // case: code = M1100
    private final static String regex03 = "^M[1-9]{2,}[0-9]+$";

    public static boolean validateCode(String code) {
        return code != null && code.matches(regex01 + "|" + regex02 + "|" + regex03);
    }

    /**
     * @effects return name
     */
    public String getName() {
        return name;
    }

    /**
     * @effects <pre>
     * if name is valid
     *   this.name = name
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setName(String name) {
        if (validateName(name)) {
            this.name = name;
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateName(String name) {
        return name != null;
    }

    /**
     * @effects return semester
     */
    public int getSemester() {
        return semester;
    }

    public static boolean validateSemester(int semester) {
        return semester >= 1;
    }

    /**
     * @effects return credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * @effects <pre>
     * if credits is valid
     *   this.credits = credits
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setCredits(int credits) {
        if (validateCredits(credits)) {
            this.credits = credits;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @effects <pre>
     * if credits is valid
     *   return true
     * else
     *   return false
     * </pre>
     */
    public static boolean validateCredits(int credits) {
        return credits >= 0;
    }

    /**
     * @effects return moduleType
     */
    public ModuleType getModuleType() {
        return moduleType;
    }

    public static boolean validateModuleType(ModuleType moduleType) {
        return moduleType != null;
    }

    /**
     * A self-validation method.
     * @effects <pre>
     * if this satisfies rep_invariant
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean repOK() {
        return validateCode(code) && validateName(name) && validateSemester(semester) && validateModuleType(moduleType);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", semester=" + semester +
                ", credits=" + credits +
                ", moduleType=" + moduleType +
                '}';
    }

    /**
     * @effects return a deep copy of this
     */
    @Override
    public Module clone() {
        Module m = null;
        try {
            m = (Module) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace(System.out);
        }
        m.code = code;
        m.name = name;
        m.semester = semester;
        m.credits = credits;
        m.moduleType = moduleType;
        return m;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return semester == module.semester && credits == module.credits && Objects.equals(code, module.code) && Objects.equals(name, module.name) && moduleType == module.moduleType;
    }

    /**
     * @Overview An enum contains two types of Module.
     */
    public enum ModuleType {
        COMPULSORY, ELECTIVE
    }
}
