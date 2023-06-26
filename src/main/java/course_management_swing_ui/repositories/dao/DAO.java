package course_management_swing_ui.repositories.dao;

import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.2
 * @Overview Interface for generic CRUD operations on a repositories for a specific type.
 */
public interface DAO<T, K> {
    /**
     * create new object in the database
     * @requires obj != null /\ conn != null /\ conn is not closed
     */
    void create(T obj, Connection conn) throws SQLException;

    /**
     * for each element of objs, create a new object in the database
     * @requires objs != null /\ conn != null /\ conn is not closed
     */
    void create(Collection<T> objs, Connection conn) throws SQLException;

    /**
     * read and return new object in the database that contains the key
     * @requires key != null /\ conn != null /\ conn is not closed
     */
    T read(K key, Connection conn) throws SQLException, NotPossibleException;

    /**
     * for each element of keys, read the corresponding row form the database. In the end, return a new list containing
     * all the retrieved rows.
     * @requires keys != null /\ conn != null /\ conn is not closed
     */
    List<T> read(Collection<K> keys, Connection conn) throws SQLException, NotPossibleException;

    /**
     * read all rows of entity T in the database
     * @requires conn != null /\ conn is not closed
     */
    List<T> all(Connection conn) throws SQLException, NotPossibleException;

    /**
     * update the row in the database that contain key of obj
     * @requires obj != null /\ obj.repOK /\ conn != null /\ conn is not closed
     */
    void update(T obj, Connection conn) throws SQLException;

    /**
     * for each element of objs, update the corresponding row in the database
     * @requires objs != null /\ obj.repOK /\ conn != null /\ conn is not closed
     */
    void update(Collection<T> objs, Connection conn) throws SQLException;

    /**
     * delete the row in the database that contain key of obj
     * @requires key != null /\ conn != null /\ conn is not closed
     */
    void delete(K key, Connection conn) throws SQLException;

    /**
     * for each element of keys, delete the corresponding row in the database
     * @requires keys != null /\ conn != null /\ conn is not closed
     */
    void delete(Collection<K> keys, Connection conn) throws SQLException;

    /**
     * clear all rows of entity T in the database
     * @requires conn != null /\ conn is not closed
     */
    void clear(Connection conn) throws SQLException;
}
