package course_management_swing_ui.repository;

import course_management_swing_ui.util.exceptions.DuplicateEntityException;

import java.util.Collection;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview A class that mediates between the domain and data mapping layers using a collection-like interface for
 * accessing domain objects. This will offer you a more sophisticated interface than the plain DAO. In other words, a
 * repository functions similarly to a DAO in that it handles data and conceals inquiries. However, it is at a higher
 * level, closer to an app's business logic.
 */
public interface Repository<T, K> {
    /**
     * add Object to the Database
     * @requires obj != null
     */
    void add(T obj) throws DuplicateEntityException;

    /**
     * add all Objects in objs to the Database
     * @requires objs != null
     */
    void addAll(Collection<T> objs) throws DuplicateEntityException;

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
     */
    void deleteAll();
}
