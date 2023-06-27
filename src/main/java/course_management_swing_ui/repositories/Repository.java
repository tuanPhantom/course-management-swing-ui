package course_management_swing_ui.repositories;

import course_management_swing_ui.util.exceptions.DuplicateEntityException;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Phan Quang Tuan
 * @version 1.3
 * @Overview A class that mediates between the domain and data mapping layers using a collection-like interface for
 * accessing domain objects. This will offer you a more sophisticated interface than the plain DAO. In other words, a
 * repositories functions similarly to a DAO in that it handles data and conceals inquiries. However, it is at a higher
 * level, closer to an app's business logic.
 */
public interface Repository<T, K> {
    /**
     * add Object to the Database
     * @requires obj != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<Void> add(T obj, Connection conn) throws DuplicateEntityException, SQLException;

    /**
     * add all Objects in objs to the Database
     * @requires objs != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<Void> addAll(Collection<T> objs, Connection conn) throws DuplicateEntityException, SQLException;

    /**
     * read and return T
     * @requires id != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<T> findById(K id, Connection conn) throws SQLException, NotPossibleException;

    /**
     * return a list of all objects of T
     * @requires ids != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<List<T>> findById(Collection<K> ids, Connection conn) throws SQLException, NotPossibleException;

    /**
     * return a list of all objects of T
     * @requires ids != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<List<T>> findAll(Connection conn) throws SQLException, NotPossibleException;

    /**
     * update the row that share the primary key with obj
     * @requires obj != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<Void> update(T obj, Connection conn) throws SQLException;

    /**
     * update all rows that share the primary key with each obj in objs
     * @requires obj != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<Void> update(Collection<T> objs, Connection conn) throws SQLException;

    /**
     * delete the row that share the primary key with obj
     * @requires obj != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<Void> delete(T obj, Connection conn) throws SQLException;

    /**
     * delete every row that share the primary key with each obj in objs
     * @requires obj != null /\ conn != null /\ conn is not closed
     */
    CompletableFuture<Void> delete(Collection<T> objs, Connection conn) throws SQLException;

    /**
     * delete all record(s) in the table of database
     * @requires conn != null /\ conn is not closed
     */
    CompletableFuture<Void> deleteAll(Connection conn) throws SQLException;
}
