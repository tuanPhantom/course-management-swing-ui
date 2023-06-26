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
import java.util.stream.Collectors;

import static course_management_swing_ui.repositories.DbContext.studentDbContext;

/**
 * @author Phan Quang Tuan
 * @version 1.2
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
    public synchronized void add(Student obj, Connection conn) throws DuplicateEntityException, SQLException {
        Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == obj.getNumericalId()).findFirst();
        if (opt.isEmpty()) {
            studentDAO.create(obj, conn);
        } else {
            throw new DuplicateEntityException("found " + obj + "in DbContext.studentDbContext");
        }
    }

    /**
     * @requires objs!=null /\ every obj in objs is not in DbContext.studentDbContext /\ conn != null /\ conn is not
     * closed
     * @modifies DbContext.studentDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void addAll(Collection<Student> objs, Connection conn) throws DuplicateEntityException, SQLException {
        for (Student obj : objs) {
            Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == obj.getNumericalId()).findFirst();
            if (opt.isPresent()) {
                throw new DuplicateEntityException("found " + obj + "in DbContext.studentDbContext");
            }
        }
        studentDAO.create(objs, conn);
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
    public synchronized Student findById(Integer id, Connection conn) throws SQLException, NotPossibleException {
        Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == id).findFirst();
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return studentDAO.read(id, conn);
        }
    }

    /**
     * @requires all id in ids are in the database /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized List<Student> findById(Collection<Integer> ids, Connection conn) throws SQLException, NotPossibleException {
        List<Student> students = new ArrayList<>();
        for (Integer id : ids) {
            students.add(findById(id, conn));
        }
        return students;
    }

    @Override
    public synchronized List<Student> findAll(Connection conn) throws SQLException, NotPossibleException {
        return studentDAO.all(conn);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void update(Student obj, Connection conn) throws SQLException {
        studentDAO.update(obj, conn);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void update(Collection<Student> objs, Connection conn) throws SQLException {
        studentDAO.update(objs, conn);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void delete(Student obj, Connection conn) throws SQLException {
        studentDAO.delete(obj.getNumericalId(), conn);
    }

    /**
     * @requires objs!=null /\ all obj in objs is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void delete(Collection<Student> objs, Connection conn) throws SQLException {
        List<Integer> ids = objs.stream().map(Student::getNumericalId).collect(Collectors.toList());
        studentDAO.delete(ids, conn);
    }

    @Override
    public synchronized void deleteAll(Connection conn) throws SQLException {
        studentDAO.clear(conn);
    }
}
