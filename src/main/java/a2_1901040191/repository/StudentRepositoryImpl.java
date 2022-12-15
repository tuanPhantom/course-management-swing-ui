package a2_1901040191.repository;

import a2_1901040191.model.Enrollment;
import a2_1901040191.model.Student;
import a2_1901040191.repository.dao.EnrollmentDAOImpl;
import a2_1901040191.repository.dao.StudentDAOImpl;
import a2_1901040191.util.exceptions.DuplicateEntityException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static a2_1901040191.repository.DbContext.enrollmentDbContext;
import static a2_1901040191.repository.DbContext.studentDbContext;
import static a2_1901040191.repository.dao.StudentDAOImpl.*;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview Implementation of Repository for Student.
 */
public class StudentRepositoryImpl implements Repository<Student, Integer> {
    private final StudentDAOImpl studentDAO = new StudentDAOImpl();
    private final EnrollmentDAOImpl enrollmentDAO = new EnrollmentDAOImpl();
    private final BatchManager batchManager = BatchManager.getInstance();

    /**
     * @requires obj!=null
     * @modifies DbContext.studentDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void add(Student obj) throws DuplicateEntityException {
        Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == obj.getNumericalId()).findFirst();
        if (opt.isEmpty()) {
            studentDAO.create(obj, false);
        } else {
            throw new DuplicateEntityException("found " + obj + "in DbContext.studentDbContext");
        }
    }

    /**
     * @requires objs!=null /\ every obj in objs is in DbContext.studentDbContext
     * @modifies DbContext.studentDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void addAll(Collection<Student> objs) throws DuplicateEntityException {
        for (Student obj : objs) {
            Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == obj.getNumericalId()).findFirst();
            if (opt.isEmpty()) {
                studentDAO.create(obj, true);
            } else {
                throw new DuplicateEntityException("found " + obj + "in DbContext.studentDbContext");
            }
        }
        batchManager.executeBatch(CREATE_STMT);
    }

    /**
     * @requires code!=null /\ code is in the database
     * @effects <pre>
     *     if there is a Student object of code in DbContext.studentDbContext
     *       return it
     *     else
     *       studentDAO.read(id)
     * </pre>
     */
    @Override
    public synchronized Student findById(Integer id) {
        Optional<Student> opt = studentDbContext.stream().filter(s -> s.getNumericalId() == id).findFirst();
        return opt.orElseGet(() -> studentDAO.read(id));
    }

    /**
     * @requires all id in ids are in the database
     */
    @Override
    public synchronized List<Student> findById(Collection<Integer> ids) {
        List<Student> students = new ArrayList<>();
        for (Integer id : ids) {
            students.add(findById(id));
        }
        return students;
    }

    @Override
    public synchronized List<Student> findAll() {
        return studentDAO.all();
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void update(Student obj) {
        studentDAO.update(obj, false);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void update(Collection<Student> objs) {
        for (Student s : objs) {
            studentDAO.update(s, true);
        }
        batchManager.executeBatch(UPDATE_STMT);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    private synchronized void delete(Student obj, boolean value) {
        studentDAO.delete(obj.getNumericalId(), value);

        Optional<Enrollment> opt = enrollmentDbContext.stream().filter(e -> e.getStudent().getNumericalId() == obj.getNumericalId()).findFirst();
        if (opt.isPresent()) {
            Enrollment enrollment = opt.get();
            enrollmentDAO.delete(enrollment.getId(), false);
        }
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void delete(Student obj) {
        delete(obj, false);
    }

    /**
     * @requires objs!=null /\ all obj in objs is in DbContext
     */
    @Override
    public synchronized void delete(Collection<Student> objs) {
        for (Student s : objs) {
            delete(s, true);
        }
        batchManager.executeBatch(DELETE_ONE_STMT);
    }

    @Override
    public synchronized void deleteAll() {
        studentDAO.clear();
        enrollmentDAO.clear();
    }
}
