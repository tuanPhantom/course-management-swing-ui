package course_management_swing_ui.repositories;

import course_management_swing_ui.models.Enrollment;
import course_management_swing_ui.models.Module;
import course_management_swing_ui.models.Student;
import course_management_swing_ui.repositories.dao.*;
import course_management_swing_ui.repositories.dao.EnrollmentDAOImpl;
import course_management_swing_ui.util.exceptions.DuplicateEntityException;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static course_management_swing_ui.repositories.DbContext.*;

/**
 * @author Phan Quang Tuan
 * @version 1.2
 * @Overview Implementation of Repository for Enrollment.
 */
public class EnrollmentRepository implements Repository<Enrollment, Integer> {
    private final EnrollmentDAOImpl enrollmentDAO = new EnrollmentDAOImpl();
    private final StudentDAOImpl studentDao = new StudentDAOImpl();
    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();

    /**
     * @requires obj!=null /\ conn != null /\ conn is not closed
     * @modifies DbContext.enrollmentDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void add(Enrollment obj, Connection conn) throws DuplicateEntityException, SQLException {
        Optional<Enrollment> opt = DbContext.enrollmentDbContext.stream().filter(e -> e.getId() == obj.getId()).findFirst();
        if (opt.isEmpty()) {
            enrollmentDAO.create(obj, conn);
        } else {
            throw new DuplicateEntityException("found " + obj + "in DbContext.enrollmentDbContext");
        }
    }

    /**
     * @requires objs!=null /\ every obj in objs is not in DbContext.enrollmentDbContext /\ conn != null /\ conn is not closed
     * @modifies DbContext.enrollmentDbContext
     * @effects insert obj into the databas only if there is no obj in DbContext
     */
    @Override
    public synchronized void addAll(Collection<Enrollment> objs, Connection conn) throws DuplicateEntityException, SQLException {
        for (Enrollment m : objs) {
            Optional<Enrollment> opt = DbContext.enrollmentDbContext.stream().filter(e -> e.getId() == m.getId()).findFirst();
            if (opt.isPresent()) {
                throw new DuplicateEntityException("found " + m + "in DbContext.enrollmentDbContext");
            }
        }
        enrollmentDAO.create(objs, conn);
    }

    /**
     * @requires id is in the database /\ conn != null /\ conn is not closed
     * @effects <pre>
     *     get Student s from DbContext.studentDbContext, otherwise read it from the database
     *     get Module m from DbContext.moduleDbContext, otherwise read it from the database
     *     create and return new Enrollment that has s and m,
     *     and it also contains id from the first row of the list returned from DAO.read(id)
     * </pre>
     */
    @Override
    public synchronized Enrollment findById(Integer id, Connection conn) throws SQLException, NotPossibleException {
        List<?> enrollmentData = enrollmentDAO.read(id, conn);
        int student_id = (int) enrollmentData.get(1);
        Optional<Student> optS = studentDbContext.stream().filter(s -> s.getNumericalId() == student_id).findFirst();
        Student s = optS.isPresent() ? optS.get() : studentDao.read(student_id, conn);

        String module_code = (String) enrollmentData.get(2);
        Optional<Module> optM = moduleDbContext.stream().filter(m -> m.getCode().equals(module_code)).findFirst();
        Module m = optM.isPresent() ? optM.get() : moduleDAO.read(module_code, conn);
        try {
            return new Enrollment(id, s, m, (double) enrollmentData.get(3), (double) enrollmentData.get(4));
        } catch (NotPossibleException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @requires ids!=null /\ every id in ids is in the database /\ conn != null /\ conn is not closed
     * @effects <pre>
     *  for every id in ids
     *     get Student s from DbContext.studentDbContext, otherwise read it from the database
     *     get Module m from DbContext.moduleDbContext, otherwise read it from the database
     *     create and add to the result list: new Enrollment that has s and m,
     *     and it also contains id from the first row of the list returned from DAO.read(id)
     * </pre>
     */
    @Override
    public synchronized List<Enrollment> findById(Collection<Integer> ids, Connection conn) throws SQLException, NotPossibleException {
        List<Enrollment> enrollments = new ArrayList<>();
        for (Integer id : ids) {
            enrollments.add(findById(id, conn));
        }
        return enrollments;
    }

    /**
     * @effects <pre>
     *  for every id in DbContext.enrollmentDbContext
     *     get Student s from DbContext.studentDbContext, otherwise read it from the database
     *     get Module m from DbContext.moduleDbContext, otherwise read it from the database
     *     create and add to the result list: new Enrollment that has s and m,
     *     and it also contains id from the first row of the list returned from DAO.read(id)
     * </pre>
     */
    @Override
    public synchronized List<Enrollment> findAll(Connection conn) throws SQLException, NotPossibleException {
        List<Enrollment> enrollments = new ArrayList<>();
        List<?> allEnrollmentData = enrollmentDAO.all(conn);
        for (Object enrollmentData : allEnrollmentData) {
            List<?> o = (List<?>) enrollmentData;
            int id = (int) o.get(0);
            int student_id = (int) o.get(1);
            Optional<Student> optS = studentDbContext.stream().filter(s -> s.getNumericalId() == student_id).findFirst();
            Student s = optS.isPresent() ? optS.get() : studentDao.read(student_id, conn);

            String module_code = (String) o.get(2);
            Optional<Module> optM = moduleDbContext.stream().filter(m -> m.getCode().equals(module_code)).findFirst();
            Module m = optM.isPresent() ? optM.get() : moduleDAO.read(module_code, conn);
            try {
                if (s != null && m != null) {
                    enrollments.add(new Enrollment(id, s, m, (double) o.get(3), (double) o.get(4)));
                }
            } catch (NotPossibleException e) {
                e.printStackTrace();
            }
        }
        return enrollments;
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void update(Enrollment obj, Connection conn) throws SQLException {
        enrollmentDAO.update(obj, conn);
    }

    /**
     * @requires objs!=null /\ objs is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void update(Collection<Enrollment> objs, Connection conn) throws SQLException {
        enrollmentDAO.update(objs, conn);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void delete(Enrollment obj, Connection conn) throws SQLException {
        enrollmentDAO.delete(obj.getId(), conn);
    }

    /**
     * @requires objs!=null /\ objs is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void delete(Collection<Enrollment> objs, Connection conn) throws SQLException {
        List<Integer> ids = objs.stream().map(Enrollment::getId).collect(Collectors.toList());
        enrollmentDAO.delete(ids, conn);
    }

    @Override
    public synchronized void deleteAll(Connection conn) throws SQLException {
        moduleDAO.clear(conn);
    }
}
