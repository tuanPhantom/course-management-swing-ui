package course_management_swing_ui.repositories;

import course_management_swing_ui.models.Module;
import course_management_swing_ui.repositories.dao.ModuleDAOImpl;
import course_management_swing_ui.util.exceptions.DuplicateEntityException;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static course_management_swing_ui.repositories.DbContext.moduleDbContext;

/**
 * @author Phan Quang Tuan
 * @version 1.2
 * @Overview Implementation of Repository for Module.
 */
public class ModuleRepository implements Repository<Module, String> {
    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();

    /**
     * @requires obj!=null /\ conn != null /\ conn is not closed
     * @modifies DbContext.moduleDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void add(Module obj, Connection conn) throws DuplicateEntityException, SQLException {
        Optional<Module> opt = moduleDbContext.stream().filter(m -> m.getCode().equals(obj.getCode())).findFirst();
        if (opt.isEmpty()) {
            moduleDAO.create(obj, conn);
        } else {
            throw new DuplicateEntityException("found " + obj + "in DbContext.moduleDbContext");
        }
    }

    /**
     * @requires objs!=null /\ every obj in objs is not in DbContext.moduleDbContext /\ conn != null /\ conn is not closed
     * @modifies DbContext.moduleDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public synchronized void addAll(Collection<Module> objs, Connection conn) throws DuplicateEntityException, SQLException {
        for (Module m : objs) {
            Optional<Module> opt = moduleDbContext.stream().filter(v -> v.getCode().equals(m.getCode())).findFirst();
            if (opt.isPresent()) {
                throw new DuplicateEntityException("found " + m + "in DbContext.moduleDbContext");
            }
        }
        moduleDAO.create(objs, conn);
    }

    /**
     * @requires code!=null /\ code is in the database /\ conn != null /\ conn is not closed
     * @effects <pre>
     *     if there is a Module object of code in DbContext.moduleDbContext
     *       return it
     *     else
     *       moduleDAO.read(code)
     * </pre>
     */
    @Override
    public synchronized Module findById(String code, Connection conn) throws SQLException, NotPossibleException {
        Optional<Module> opt = moduleDbContext.stream().filter(v -> v.getCode().equals(code)).findFirst();
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return moduleDAO.read(code, conn);
        }
    }

    /**
     * @requires codes!=null /\ codes is in the database /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized List<Module> findById(Collection<String> codes, Connection conn) throws SQLException, NotPossibleException {
        List<Module> modules = new ArrayList<>();
        for (String code : codes) {
            modules.add(findById(code, conn));
        }
        return modules;
    }

    @Override
    public synchronized List<Module> findAll(Connection conn) throws SQLException, NotPossibleException {
        return moduleDAO.all(conn);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void update(Module obj, Connection conn) throws SQLException {
        moduleDAO.update(obj, conn);
    }

    /**
     * @requires objs!=null /\ objs is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void update(Collection<Module> objs, Connection conn) throws SQLException {
        moduleDAO.update(objs, conn);
    }

    /**
     * @requires obj!=null /\ obj is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void delete(Module obj, Connection conn) throws SQLException {
        moduleDAO.delete(obj.getCode(), conn);
    }

    /**
     * @requires objs!=null /\ all obj in objs is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public synchronized void delete(Collection<Module> objs, Connection conn) throws SQLException {
        List<String> ids = objs.stream().map(Module::getCode).collect(Collectors.toList());
        moduleDAO.delete(ids, conn);
    }

    @Override
    public synchronized void deleteAll(Connection conn) throws SQLException {
        moduleDAO.clear(conn);
    }
}
