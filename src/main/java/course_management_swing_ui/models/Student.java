package course_management_swing_ui.models;

import course_management_swing_ui.util.Safe;
import course_management_swing_ui.util.dto.*;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Requires JDK >= 11
 * @Overview A class represent a student who studies in the university
 * @attributes <pre>
 *  id              String
 *  name            String
 *  dob             LocalDate
 *  address         String
 *  email           String
 * </pre>
 * @Object a typical student is  S:<id, name, dob, address, email>
 * AF(c) = S:<"S20xx", name, dob, address, email>
 *
 * @rep_invariant
 * id != null /\ id = 'S' + '{x | x >= 2022}' /\
 *  name !=null /\
 *  dob is after 1970-01-01 (YYYY-MM-DD) /\
 *  address != null /\
 *  email != null /\ email.matches("^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$")
 *
 * @version 1.0
 * @author Phan Quang Tuan
 */
public class Student implements Cloneable, Comparable<Student> {
    private static final int DEFAULT_YEAR_COUNT = 2022;
    public static final List<Integer> idCount = new ArrayList<>();

    @DisplayInTable
    private String id;
    @DisplayInTable
    private String name;
    @DisplayInTable
    private LocalDate dob;
    @DisplayInTable
    private String address;
    @DisplayInTable
    private String email;

    /**
     * @modifies idCount
     * @effects <pre>
     * if pre_conditions are valid
     * 	initialize this as S:<id, name, dob, address, email>
     * else
     * 	throw NotPossibleException</pre>
     */
    public Student(String name, LocalDate dob, String address, String email) throws NotPossibleException {
        if (!validateName(name)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid name: \"" + name + "\"");
        }

        if (!validateDob(dob)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid dob: \"" + dob + "\"");
        }

        if (!validateAddress(address)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid address: \"" + address + "\"");
        }

        if (!validateEmail(email)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid email: \"" + email + "\"");
        }

        int newId;
        synchronized (this) {
            newId = idCount.size() != 0 ? idCount.get(idCount.size() - 1) + 1 : 2022;
            addToIdCount(newId);
        }

        this.id = "S" + newId;
        this.name = name;
        this.dob = LocalDate.parse(dob.toString());
        this.address = address;
        this.email = email;
    }

    private synchronized void addToIdCount(int id) {
        if (!idCount.contains(id)) {
            idCount.add(id);
        }
    }

    /**
     * @requires this.id was fetched from the database
     * @effects <pre>
     * if pre_conditions are valid
     * 	initialize this as S:<id, name, dob, address, email>
     * else
     * 	throw NotPossibleException</pre>
     */
    @Safe
    public Student(int id, String name, LocalDate dob, String address, String email) throws NotPossibleException {
        if (!validateId("S" + id)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid id: \"S" + id + "\"");
        }

        if (!validateName(name)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid name: \"" + name + "\"");
        }

        if (!validateDob(dob)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid dob: \"" + dob + "\"");
        }

        if (!validateAddress(address)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid address: \"" + address + "\"");
        }

        if (!validateEmail(email)) {
            throw new NotPossibleException(this.getClass().getSimpleName() + ": Init: Invalid email: \"" + email + "\"");
        }

        this.id = "S" + id;
        this.name = name;
        this.dob = LocalDate.parse(dob.toString());
        this.address = address;
        this.email = email;
    }

    /**
     * @effects return id
     */
    public String getId() {
        return id;
    }

    /**
     * @effects return the numerical part of id
     */
    public int getNumericalId() {
        return Integer.parseInt(id.substring(1));
    }

    /**
     * @effects <pre>
     * if id is valid
     *   return true
     * else
     *   return false
     * </pre>
     */
    public static boolean validateId(String id) {
        return id != null && id.matches("^S[0-9]{4,}$") && Integer.parseInt(id.substring(1)) >= DEFAULT_YEAR_COUNT;
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

    /**
     * @effects <pre>
     * if name is valid
     *   return true
     * else
     *   return false
     * </pre>
     */
    public static boolean validateName(String name) {
        return name != null;
    }

    /**
     * @effects return dob
     */
    public LocalDate getDob() {
        return LocalDate.parse(dob.toString());
    }

    /**
     * @effects <pre>
     * if dob is valid
     *   this.dob = dob
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setDob(LocalDate dob) {
        if (validateDob(dob)) {
            this.dob = LocalDate.parse(dob.toString());
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateDob(LocalDate dob) {
        return dob != null && dob.isAfter(LocalDate.EPOCH);
    }

    /**
     * @effects return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @effects <pre>
     * if address is valid
     *   this.address = address
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setAddress(String address) {
        if (validateAddress(address)) {
            this.address = address;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @effects <pre>
     * if address is valid
     *   return true
     * else
     *   return false
     * </pre>
     */
    public static boolean validateAddress(String address) {
        return address != null;
    }

    /**
     * @effects return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @effects <pre>
     * if email is valid
     *   this.email = email
     *   return true
     * else
     *   return false
     * </pre>
     */
    public boolean setEmail(String email) {
        if (validateEmail(email)) {
            this.email = email;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @effects <pre>
     * if email is valid
     *   return true
     * else
     *   return false
     * </pre>
     */
    public static boolean validateEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
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
        return validateId(id) && validateName(name) && validateDob(dob) && validateAddress(address) && validateEmail(email);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    /**
     * @effects return a deep copy of this
     */
    @Override
    public Student clone() {
        Student s = null;
        try {
            s = (Student) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace(System.out);
        }
        s.id = id;
        s.name = name;
        s.dob = LocalDate.parse(dob.toString());
        s.address = address;
        s.email = email;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(name, student.name) && Objects.equals(dob, student.dob) && Objects.equals(address, student.address) && Objects.equals(email, student.email);
    }

    @Override
    public int compareTo(Student o) {
        if (o == null) {
            return 1;
        } else {
            int thisId = Integer.parseInt(id.substring(1));
            int oId = Integer.parseInt(o.id.substring(1));
            return Integer.compare(thisId, oId);
        }
    }
}
