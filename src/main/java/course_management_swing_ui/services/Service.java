package course_management_swing_ui.services;

import java.util.Collection;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.2
 * @Overview This interface represents the middle layer of architecture, between controller and repository. It
 * implements business logic and calculations This layer also validates the input conditions before calling a method
 * from the data layer. This ensures the data input is correct before proceeding, and can often ensure that the outputs
 * are correct as well.
 */
public interface Service<T, K> {
    /**
     * add Object to the Database
     * @requires obj != null
     */
    void add(T obj);

    /**
     * add all Objects in objs to the Database
     * @requires objs != null
     */
    void addAll(Collection<T> objs);

    /**
     * read and return T
     * @requires id != null
     */
    T findById(K id);

    /**
     * return a list of all objects of T
     * @requires ids != null
     */
    List<T> findById(Collection<K> ids);

    /**
     * return a list of all objects of T
     * @requires ids != null
     */
    List<T> findAll();

    /**
     * update the row that share the primary key with obj
     * @requires obj != null
     */
    void update(T obj);

    /**
     * update all rows that share the primary key with each obj in objs
     * @requires obj != null
     */
    void update(Collection<T> objs);

    /**
     * delete the row that share the primary key with obj
     * @requires obj != null
     */
    void delete(T obj);

    /**
     * delete every row that share the primary key with each obj in objs
     * @requires obj != null
     */
    void delete(Collection<T> objs);

    /**
     * delete all record(s) in the table of database
     * @requires conn != null
     */
    void deleteAll();
}
