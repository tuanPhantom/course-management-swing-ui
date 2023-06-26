package course_management_swing_ui.services;

import course_management_swing_ui.models.Enrollment;
import course_management_swing_ui.models.Module;
import course_management_swing_ui.repositories.DbContext;
import course_management_swing_ui.repositories.EnrollmentRepository;
import course_management_swing_ui.repositories.ModuleRepository;
import course_management_swing_ui.repositories.db.DbConnect;
import course_management_swing_ui.util.exceptions.DuplicateEntityException;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Phan Quang Tuan
 * @version 1.2
 * @Overview Implementation of Service for Module.
 */
public class ModuleService implements Service<Module, String> {
    private final ModuleRepository moduleRepository = new ModuleRepository();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();
    /**
     * add Object to the Database
     * @param obj
     * @requires obj != null
     */
    @Override
    public void add(Module obj) {
        try (Connection conn = DbConnect.getConnection()) {
            moduleRepository.add(obj, conn);
        } catch (SQLException | DuplicateEntityException e) {
            e.printStackTrace();
        }
    }

    /**
     * add all Objects in objs to the Database
     * @param objs
     * @requires objs != null
     */
    @Override
    public void addAll(Collection<Module> objs) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                moduleRepository.addAll(objs, conn);
                conn.commit();
            } catch (SQLException | DuplicateEntityException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * read and return T
     * @param id
     * @requires id != null
     */
    @Override
    public Module findById(String id) {
        try (Connection conn = DbConnect.getConnection()) {
            return moduleRepository.findById(id, conn);
        } catch (SQLException | NotPossibleException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * return a list of all objects of T
     * @param ids
     * @requires ids != null
     */
    @Override
    public List<Module> findById(Collection<String> ids) {
        List<Module> modules = new ArrayList<>();
        try (Connection conn = DbConnect.getConnection()) {
            modules.addAll(moduleRepository.findById(ids, conn));
        } catch (SQLException | NotPossibleException e) {
            e.printStackTrace();
        }
        return modules;
    }

    /**
     * return a list of all objects of T
     * @requires ids != null
     */
    @Override
    public List<Module> findAll() {
        List<Module> modules = new ArrayList<>();
        try (Connection conn = DbConnect.getConnection()) {
            modules.addAll(moduleRepository.findAll(conn));
        } catch (SQLException | NotPossibleException e) {
            e.printStackTrace();
        }
        return modules;
    }

    /**
     * update the row that share the primary key with obj
     * @param obj
     * @requires obj != null
     */
    @Override
    public void update(Module obj) {
        try (Connection conn = DbConnect.getConnection()) {
            moduleRepository.update(obj, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * update all rows that share the primary key with each obj in objs
     * @param objs
     * @requires obj != null
     */
    @Override
    public void update(Collection<Module> objs) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                moduleRepository.update(objs, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete the row that share the primary key with obj
     * @param obj
     * @requires obj != null
     */
    @Override
    public void delete(Module obj) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                moduleRepository.delete(obj, conn);
                Optional<Enrollment> opt = DbContext.enrollmentDbContext.stream().filter(e -> e.getModule().getCode().equals(obj.getCode())).findFirst();
                if (opt.isPresent()) {
                    Enrollment enrollment = opt.get();
                    enrollmentRepository.delete(enrollment, conn);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete every row that share the primary key with each obj in objs
     * @param objs
     * @requires obj != null
     */
    @Override
    public void delete(Collection<Module> objs) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                moduleRepository.delete(objs, conn);

                // delete enrollments
                List<String> codes = objs.stream().map(Module::getCode).collect(Collectors.toList());
                List<Enrollment> enrollments = DbContext.enrollmentDbContext.stream().filter(e -> codes.contains(e.getModule().getCode())).collect(Collectors.toList());
                enrollmentRepository.delete(enrollments, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete all record(s) in the table of database
     * @requires conn != null
     */
    @Override
    public void deleteAll() {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                moduleRepository.deleteAll(conn);
                enrollmentRepository.deleteAll(conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
