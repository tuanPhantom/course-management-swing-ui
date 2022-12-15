package a2_1901040191.repository;

import a2_1901040191.model.Enrollment;
import a2_1901040191.model.Module;
import a2_1901040191.model.Student;
import a2_1901040191.repository.dao.*;
import a2_1901040191.repository.dao.EnrollmentDAOImpl;
import a2_1901040191.util.exceptions.DuplicateEntityException;
import a2_1901040191.util.exceptions.NotPossibleException;

import java.util.*;

import static a2_1901040191.repository.DbContext.*;
import static a2_1901040191.repository.dao.EnrollmentDAOImpl.*;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview Implementation of Repository for Enrollment.
 */
public class EnrollmentRepositoryImpl implements Repository<Enrollment, Integer> {
    private final EnrollmentDAOImpl enrollmentDAO = new EnrollmentDAOImpl();
    private final StudentDAOImpl studentDao = new StudentDAOImpl();
    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
    private final BatchManager batchManager = BatchManager.getInstance();

    /**
     * @requires obj!=null
     * @modifies DbContext.enrollmentDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void add(Enrollment obj) throws DuplicateEntityException {
        Optional<Enrollment> opt = DbContext.enrollmentDbContext.stream().filter(e -> e.getId() == obj.getId()).findFirst();
        if (opt.isEmpty()) {
            enrollmentDAO.create(obj, false);
        } else {
            throw new DuplicateEntityException("found " + obj + "in DbContext.enrollmentDbContext");
        }
    }

    /**
     * @requires objs!=null /\ every obj in objs is in DbContext.enrollmentDbContext
     * @modifies DbContext.enrollmentDbContext
     * @effects insert obj into the databas only if there is no obj in DbContext
     */
    @Override
    public synchronized void addAll(Collection<Enrollment> objs) throws DuplicateEntityException {
        for (Enrollment m : objs) {
            Optional<Enrollment> opt = DbContext.enrollmentDbContext.stream().filter(e -> e.getId() == m.getId()).findFirst();
            if (opt.isEmpty()) {
                enrollmentDAO.create(m, true);
            } else {
                throw new DuplicateEntityException("found " + m + "in DbContext.enrollmentDbContext");
            }
        }
        batchManager.executeBatch(CREATE_STMT);
    }

    /**
     * @requires id is in the database
     * @effects <pre>
     *     get Student s from DbContext.studentDbContext, otherwise read it from the database
     *     get Module m from DbContext.moduleDbContext, otherwise read it from the database
     *     create and return new Enrollment that has s and m,
     *     and it also contains id from the first row of the list returned from DAO.read(id)
     * </pre>
     */
    @Override
    public synchronized Enrollment findById(Integer id) {
        List<?> enrollmentData = enrollmentDAO.read(id);
        int student_id = (int) enrollmentData.get(1);
        Optional<Student> optS = studentDbContext.stream().filter(s -> s.getNumericalId() == student_id).findFirst();
        Student s = optS.orElseGet(() -> studentDao.read(student_id));

        String module_code = (String) enrollmentData.get(2);
        Optional<Module> optM = moduleDbContext.stream().filter(m -> m.getCode().equals(module_code)).findFirst();
        Module m = optM.orElseGet(() -> moduleDAO.read(module_code));
        try {
            return new Enrollment(id, s, m, (double) enrollmentData.get(3), (double) enrollmentData.get(4));
        } catch (NotPossibleException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @requires ids!=null /\ every id in ids is in the database
     * @effects <pre>
     *  for every id in ids
     *     get Student s from DbContext.studentDbContext, otherwise read it from the database
     *     get Module m from DbContext.moduleDbContext, otherwise read it from the database
     *     create and add to the result list: new Enrollment that has s and m,
     *     and it also contains id from the first row of the list returned from DAO.read(id)
     * </pre>
     */
    @Override
    public synchronized List<Enrollment> findById(Collection<Integer> ids) {
        List<Enrollment> enrollments = new ArrayList<>();
        for (Integer id : ids) {
            enrollments.add(findById(id));
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
    public synchronized List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        List<?> allEnrollmentData = enrollmentDAO.all();
        for (Object enrollmentData : allEnrollmentData) {
            List<?> o = (List<?>) enrollmentData;
            int id = (int) o.get(0);
            int student_id = (int) o.get(1);
            Optional<Student> optS = studentDbContext.stream().filter(s -> s.getNumericalId() == student_id).findFirst();
            Student s = optS.orElseGet(() -> studentDao.read(student_id));

            String module_code = (String) o.get(2);
            Optional<Module> optM = moduleDbContext.stream().filter(m -> m.getCode().equals(module_code)).findFirst();
            Module m = optM.orElseGet(() -> moduleDAO.read(module_code));
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
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void update(Enrollment obj) {
        enrollmentDAO.update(obj, false);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void update(Collection<Enrollment> objs) {
        for (Enrollment m : objs) {
            enrollmentDAO.update(m, true);
        }
        batchManager.executeBatch(UPDATE_STMT);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void delete(Enrollment obj) {
        enrollmentDAO.delete(obj.getId(), false);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void delete(Collection<Enrollment> objs) {
        for (Enrollment m : objs) {
            enrollmentDAO.delete(m.getId(), true);
        }
        batchManager.executeBatch(DELETE_ONE_STMT);
    }

    @Override
    public synchronized void deleteAll() {
        moduleDAO.clear();
    }
}
