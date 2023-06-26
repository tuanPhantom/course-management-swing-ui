package course_management_swing_ui.repository;

import course_management_swing_ui.model.Enrollment;
import course_management_swing_ui.model.Module;
import course_management_swing_ui.repository.dao.EnrollmentDAOImpl;
import course_management_swing_ui.repository.dao.ModuleDAOImpl;
import course_management_swing_ui.util.exceptions.DuplicateEntityException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static course_management_swing_ui.repository.DbContext.enrollmentDbContext;
import static course_management_swing_ui.repository.DbContext.moduleDbContext;
import static course_management_swing_ui.repository.dao.ModuleDAOImpl.*;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview Implementation of Repository for Module.
 */
public class ModuleRepositoryImpl implements Repository<Module, String> {
    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
    private final EnrollmentDAOImpl enrollmentDAO = new EnrollmentDAOImpl();
    private final BatchManager batchManager = BatchManager.getInstance();

    /**
     * @requires obj!=null
     * @modifies DbContext.moduleDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void add(Module obj) throws DuplicateEntityException {
        Optional<Module> opt = moduleDbContext.stream().filter(m -> m.getCode().equals(obj.getCode())).findFirst();
        if (opt.isEmpty()) {
            moduleDAO.create(obj, false);
        } else {
            throw new DuplicateEntityException("found " + obj + "in DbContext.moduleDbContext");
        }
    }

    /**
     * @requires objs!=null /\ every obj in objs is in DbContext.moduleDbContext
     * @modifies DbContext.moduleDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void addAll(Collection<Module> objs) throws DuplicateEntityException {
        for (Module m : objs) {
            Optional<Module> opt = moduleDbContext.stream().filter(v -> v.getCode().equals(m.getCode())).findFirst();
            if (opt.isEmpty()) {
                moduleDAO.create(m, true);
            } else {
                throw new DuplicateEntityException("found " + m + "in DbContext.moduleDbContext");
            }
        }
        batchManager.executeBatch(CREATE_STMT);
    }

    /**
     * @requires code!=null /\ code is in the database
     * @effects <pre>
     *     if there is a Module object of code in DbContext.moduleDbContext
     *       return it
     *     else
     *       moduleDAO.read(code)
     * </pre>
     */
    @Override
    public synchronized Module findById(String code) {
        Optional<Module> opt = moduleDbContext.stream().filter(v -> v.getCode().equals(code)).findFirst();
        return opt.orElseGet(() -> moduleDAO.read(code));
    }

    /**
     * @requires codes!=null /\ codes is in the database
     */
    @Override
    public synchronized List<Module> findById(Collection<String> codes) {
        List<Module> modules = new ArrayList<>();
        for (String code : codes) {
            modules.add(moduleDAO.read(code));
        }
        return modules;
    }

    @Override
    public synchronized List<Module> findAll() {
        return moduleDAO.all();
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void update(Module obj) {
        moduleDAO.update(obj, false);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void update(Collection<Module> objs) {
        for (Module m : objs) {
            moduleDAO.update(m, true);
        }
        batchManager.executeBatch(UPDATE_STMT);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    private synchronized void delete(Module obj, boolean value) {
        if (moduleDAO.delete(obj.getCode(), value)) {
            Optional<Enrollment> opt = enrollmentDbContext.stream().filter(e -> e.getModule().getCode().equals(obj.getCode())).findFirst();
            if (opt.isPresent()) {
                Enrollment enrollment = opt.get();
                enrollmentDAO.delete(enrollment.getId(), false);
            }
        }
    }

    /**
     * @requires obj!=null /\ obj is in DbContext
     */
    @Override
    public synchronized void delete(Module obj) {
        delete(obj, false);
    }

    /**
     * @requires objs!=null /\ all obj in objs is in DbContext
     */
    @Override
    public synchronized void delete(Collection<Module> objs) {
        for (Module m : objs) {
            delete(m, true);
        }
        batchManager.executeBatch(DELETE_ONE_STMT);
    }

    @Override
    public synchronized void deleteAll() {
        if (!moduleDAO.clear()) return;
        enrollmentDAO.clear();
    }
}
