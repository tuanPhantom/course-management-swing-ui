package a2_1901040191.repository.dao;

import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview Interface for generic CRUD operations on a repository for a specific type.
 */
public interface DAO<T, K> {
    /**
     * create new object in the database
     * @requires obj != null
     */
    void create(T obj, boolean addToBatch);

    /**
     * read and return new object in the database that contains the key
     * @requires key != null
     */
    T read(K key);

    /**
     * read all rows of entity T in the database
     */
    List<T> all();

    /**
     * update the row in the database that contain key of obj
     * @requires obj != null /\ obj.repOK
     */
    void update(T obj, boolean addToBatch);

    /**
     * delete the row in the database that contain key of obj
     * @requires key != null
     */
    void delete(K key, boolean addToBatch);

    /**
     * clear all rows of entity T in the database
     */
    void clear();

    /**
     * reconnect to the database
     */
    void connect();
}
