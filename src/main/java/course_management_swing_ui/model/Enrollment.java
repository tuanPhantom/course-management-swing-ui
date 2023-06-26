package course_management_swing_ui.model;

import course_management_swing_ui.util.dto.*;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview Entity that represents the act of a Student officially joining a Module
 * @attributes <pre>
 *  id                  int
 *  student             Student
 *  module              Module
 *  internalMark        double
 *  examinationMark     double
 *  finalGrade          char
 * </pre>
 * @Object AF(c) = < id, student, module, internalMark, examinationMark, finalGrade>
 * @rep_invariant <pre>
 *   id >= 1 /\
 *   student != null && student.repOK() /\
 *   module != null && module.repOK() /\
 *   internalMark >= 0 && internalMark <= 10 /\
 *   examinationMark >= 0 && examinationMark <= 10
 *   </pre>
 */
public class Enrollment implements Cloneable {
    @DisplayInTable
    private int id;
    @DisplayInTable(skip = true)
    private Student student;
    @DisplayInTable(skip = true)
    private Module module;
    @DisplayInTable
    private double internalMark;
    @DisplayInTable
    private double examinationMark;
    private double aggregatedMark;  // derived attribute
    @DisplayInTable
    private char finalGrade;

    public Enrollment(int id, Student student, Module module, double internalMark, double examinationMark) throws NotPossibleException {
        if (!validateId(id)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid id: \"" + id + "\"");
        }

        if (!validateStudent(student)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid student: \"" + student + "\"");
        }

        if (!validateModule(module)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid module: \"" + module + "\"");
        }

        if (!validateMark(internalMark)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid internalMark: \"" + internalMark + "\"");
        }

        if (!validateMark(examinationMark)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid examinationMark: \"" + examinationMark + "\"");
        }

        this.id = id;
        this.student = student.clone();
        this.module = module.clone();
        this.internalMark = internalMark;
        this.examinationMark = examinationMark;
        this.finalGrade = evaluateGrade();
    }

    private double calculateAggregatedMark() {
        return 0.4 * internalMark + 0.6 * examinationMark;
    }

    /**
     * @requires be assigned and called in the constructor
     * @modifies aggregatedMark
     * @effects
     */
    private char evaluateGrade() {
        aggregatedMark = calculateAggregatedMark();
        if (Double.compare(aggregatedMark, 8.5) >= 0) {
            return Grade.E;
        } else if (Double.compare(aggregatedMark, 7) >= 0) {
            return Grade.G;
        } else if (Double.compare(aggregatedMark, 5) >= 0) {
            return Grade.P;
        } else {
            return Grade.F;
        }
    }

    /**
     * @effects return id
     */
    public int getId() {
        return id;
    }

    /**
     * @effects <pre>
     * if id is valid
     *   return true
     * else
     *   return false
     * </pre>
     */
    public static boolean validateId(int id) {
        return id >= 1;
    }

    /**
     * @effects return a copy of student
     */
    public Student getStudent() {
        return student.clone();
    }

    /**
     * @effects <pre>
     * if student is valid
     *   this.student = student
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setStudent(Student student) {
        if (validateStudent(student)) {
            this.student = student.clone();
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateStudent(Student student) {
        return student != null && student.repOK();
    }

    /**
     * @effects return a copy of module
     */
    public Module getModule() {
        return module.clone();
    }

    /**
     * @effects <pre>
     * if module is valid
     *   this.module = module
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setModule(Module module) {
        if (validateModule(module)) {
            this.module = module.clone();
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateModule(Module module) {
        return module != null && module.repOK();
    }

    /**
     * @effects return internalMark
     */
    public double getInternalMark() {
        return internalMark;
    }

    /**
     * @modifies finalGrade
     * @effects <pre>
     * if internalMark is valid
     *   this.internalMark = internalMark
     *   evaluate finalGrade
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setInternalMark(double internalMark) {
        if (validateMark(internalMark)) {
            this.internalMark = internalMark;
            this.finalGrade = evaluateGrade();
            return true;
        } else {
            return false;
        }
    }

    /**
     * @effects return examinationMark
     */
    public double getExaminationMark() {
        return examinationMark;
    }

    /**
     * @modifies finalGrade
     * @effects <pre>
     * if examinationMark is valid
     *   this.examinationMark = examinationMark
     *   evaluate finalGrade
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setExaminationMark(double examinationMark) {
        if (validateMark(examinationMark)) {
            this.examinationMark = examinationMark;
            this.finalGrade = evaluateGrade();
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateMark(double mark) {
        return mark >= 0 && mark <= 10;
    }

    /**
     * @effects return aggregatedMark
     */
    public double getAggregatedMark() {
        return aggregatedMark;
    }

    /**
     * @effects return finalGrade
     */
    public char getFinalGrade() {
        return finalGrade;
    }


    public static boolean validateFinalGrade(char finalGrade) {
        List<Character> gradeList = new ArrayList<>();
        Field[] fields = Grade.class.getFields();
        for (Field f : fields) {
            try {
                gradeList.add((Character) f.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.out);
            }
        }
        return gradeList.contains(finalGrade);
    }

    /**
     * A self-validation method.
     *
     * @effects <pre>
     * if this satisfies rep_invariant
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean repOK() {
        return validateStudent(student) && validateModule(module) && validateMark(internalMark) && validateMark(examinationMark) && validateFinalGrade(finalGrade) && validateId(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id=" + id +
                "student=" + student +
                ", module=" + module +
                ", internalMark=" + internalMark +
                ", examinationMark=" + examinationMark +
                ", finalGrade='" + finalGrade + '\'' +
                '}';
    }

    /**
     * @effects return a deep copy of this
     */
    @Override
    public Enrollment clone() {
        Enrollment e = null;
        try {
            e = (Enrollment) super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace(System.out);
        }
        e.id = id;
        e.student = student.clone();
        e.module = module.clone();
        e.internalMark = internalMark;
        e.examinationMark = examinationMark;
        e.finalGrade = finalGrade;
        return e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return id == that.id && Double.compare(that.internalMark, internalMark) == 0 && Double.compare(that.examinationMark, examinationMark) == 0 && finalGrade == that.finalGrade && Objects.equals(student, that.student) && Objects.equals(module, that.module);
    }

    public static class Grade {
        public static final char E = 'E';
        public static final char G = 'G';
        public static final char P = 'P';
        public static final char F = 'F';
    }
}
