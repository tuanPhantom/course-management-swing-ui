package a2_1901040191.model;

import a2_1901040191.util.Safe;
import a2_1901040191.util.dto.*;
import a2_1901040191.util.exceptions.NotPossibleException;

import java.util.Objects;

public class ElectiveModule extends Module {
    @DisplayInTable
    private String department;

    /**
     * @modifies idCountMap
     * @effects <pre>
     * if rep_invariant are satisfied
     * 	initialize this as < <"M" + semester * 100 + idCount> , name, semester, ELECTIVE, department>
     * else
     * 	throw NotPossibleException</pre>
     */
    public ElectiveModule(String name, int semester, int credits, String department) throws NotPossibleException {
        super(name, semester, credits, ModuleType.ELECTIVE);
        this.department = department;
    }

    /**
     * @requires this.code was fetched from the database
     * @effects <pre>
     * if rep_invariant are satisfied
     * 	initialize this as < code, name, semester, ELECTIVE, department>
     * else
     * 	throw NotPossibleException</pre>
     */
    @Safe
    public ElectiveModule(String code, String name, int semester, int credits, String department) throws NotPossibleException {
        super(code, name, semester, credits, ModuleType.ELECTIVE);
        this.department = department;
    }

    /**
     * @effects return department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @effects <pre>
     * if department is valid
     *   this.department = department
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setDepartment(String department) {
        if (validateDepartment(department)) {
            this.department = department;
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateDepartment(String department) {
        return department != null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "code='" + getCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", semester=" + getSemester() +
                ", credits=" + getCredits() +
                ", moduleType=" + getModuleType() +
                ", department=" + department +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ElectiveModule that = (ElectiveModule) o;
        return super.equals(o) && Objects.equals(department, that.department);
    }
}
