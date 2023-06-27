package course_management_swing_ui.repositories;

import course_management_swing_ui.models.Student;
import course_management_swing_ui.repositories.dao.StudentDAOImpl;
import course_management_swing_ui.util.exceptions.DuplicateEntityException;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static course_management_swing_ui.repositories.DbContext.studentDbContext;

/**
 * @author Phan Quang Tuan
 * @version 1.3
 * @Overview Implementation of Repository for Student.
 */
public class StudentRepository implements Repository<Student, Integer> {
    private final StudentDAOImpl studentDAO = new StudentDAOImpl();

    /**
     * @requires obj!=null /\ conn != null /\ conn is not closed
     * @modifies DbContext.studentDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public CompletableFuture<Void> add(Student obj, Connection conn) throws DuplicateEntityException, SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == obj.getNumericalId()).findFirst();
                if (opt.isEmpty()) {
                    studentDAO.create(obj, conn);
                } else {
                    throw new DuplicateEntityException("found " + obj + "in DbContext.studentDbContext");
                }
            } catch (SQLException | DuplicateEntityException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires objs!=null /\ every obj in objs is not in DbContext.studentDbContext /\ conn != null /\ conn is not
     * closed
     * @modifies DbContext.studentDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public CompletableFuture<Void> addAll(Collection<Student> objs, Connection conn) throws DuplicateEntityException, SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                for (Student obj : objs) {
                    Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == obj.getNumericalId()).findFirst();
                    if (opt.isPresent()) {
                        throw new DuplicateEntityException("found " + obj + "in DbContext.studentDbContext");
                    }
                }
                studentDAO.create(objs, conn);
            } catch (SQLException | DuplicateEntityException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires code!=null /\ code is in the database /\ conn != null /\ conn is not closed
     * @effects <pre>
     *     if there is a Student object of code in DbContext.studentDbContext
     *       return it
     *     else
     *       studentDAO.read(id)
     * </pre>
     */
    @Override
    public CompletableFuture<Student> findById(Integer id, Connection conn) throws SQLException, NotPossibleException {
        CompletableFuture<Student> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == id).findFirst();
                if (opt.isPresent()) {
                    return opt.get();
                } else {
                    return studentDAO.read(id, conn);
                }
            } catch (SQLException | NotPossibleException e) {
                future.completeExceptionally(e);
            }
            return null;
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires all id in ids are in the database /\ conn != null /\ conn is not closed
     */
    @Override
    public CompletableFuture<List<Student>> findById(Collection<Integer> ids, Connection conn) throws SQLException, NotPossibleException {
        CompletableFuture<List<Student>> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                List<Student> students = new ArrayList<>();
                List<CompletableFuture<Student>> tasks = new ArrayList<>();
                for (Integer id : ids) {
                    tasks.add(findById(id, conn));
                }
                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).get();
                for (CompletableFuture<Student> task : tasks) {
                    students.add(task.get());
                }
                return students;
            } catch (SQLException | NotPossibleException | ExecutionException | InterruptedException e) {
                future.completeExceptionally(e);
            }
            return null;
        }).thenAccept(future::complete);
        return future;
    }

    @Override
    public CompletableFuture<List<Student>> findAll(Connection conn) throws SQLException, NotPossibleException {
        CompletableFuture<List<Student>> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return studentDAO.all(conn);
            } catch (SQLException | NotPossibleException e) {
                future.completeExceptionally(e);
            }
            return null;
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public CompletableFuture<Void> update(Student obj, Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                studentDAO.update(obj, conn);
            } catch (SQLException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public CompletableFuture<Void> update(Collection<Student> objs, Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                studentDAO.update(objs, conn);
            } catch (SQLException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public CompletableFuture<Void> delete(Student obj, Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                studentDAO.delete(obj.getNumericalId(), conn);
            } catch (SQLException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires objs!=null /\ all obj in objs is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public CompletableFuture<Void> delete(Collection<Student> objs, Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                List<Integer> ids = objs.stream().map(Student::getNumericalId).collect(Collectors.toList());
                studentDAO.delete(ids, conn);
            } catch (SQLException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }

    @Override
    public CompletableFuture<Void> deleteAll(Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                studentDAO.clear(conn);
            } catch (SQLException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }
}
