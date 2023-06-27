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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static course_management_swing_ui.repositories.DbContext.moduleDbContext;

/**
 * @author Phan Quang Tuan
 * @version 1.3
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
    public CompletableFuture<Void> add(Module obj, Connection conn) throws DuplicateEntityException, SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                Optional<Module> opt = moduleDbContext.stream().filter(m -> m.getCode().equals(obj.getCode())).findFirst();
                if (opt.isEmpty()) {
                    moduleDAO.create(obj, conn);
                } else {
                    throw new DuplicateEntityException("found " + obj + "in DbContext.moduleDbContext");
                }
            } catch (SQLException | DuplicateEntityException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires objs!=null /\ every obj in objs is not in DbContext.moduleDbContext /\ conn != null /\ conn is not
     * closed
     * @modifies DbContext.moduleDbContext
     * @effects insert obj into the database only if there is no obj in DbContext
     */
    @Override
    public CompletableFuture<Void> addAll(Collection<Module> objs, Connection conn) throws DuplicateEntityException, SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                for (Module m : objs) {
                    Optional<Module> opt = moduleDbContext.stream().filter(v -> v.getCode().equals(m.getCode())).findFirst();
                    if (opt.isPresent()) {
                        throw new DuplicateEntityException("found " + m + "in DbContext.moduleDbContext");
                    }
                }
                moduleDAO.create(objs, conn);
            } catch (SQLException | DuplicateEntityException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
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
    public CompletableFuture<Module> findById(String code, Connection conn) throws SQLException, NotPossibleException {
        CompletableFuture<Module> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Module> opt = moduleDbContext.stream().filter(v -> v.getCode().equals(code)).findFirst();
                if (opt.isPresent()) {
                    return opt.get();
                } else {
                    return moduleDAO.read(code, conn);
                }
            } catch (SQLException | NotPossibleException e) {
                future.completeExceptionally(e);
            }
            return null;
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires codes!=null /\ codes is in the database /\ conn != null /\ conn is not closed
     */
    @Override
    public CompletableFuture<List<Module>> findById(Collection<String> codes, Connection conn) throws SQLException, NotPossibleException {
        CompletableFuture<List<Module>> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                List<Module> modules = new ArrayList<>();
                List<CompletableFuture<Module>> tasks = new ArrayList<>();
                for (String code : codes) {
                    tasks.add(findById(code, conn));
                }
                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]));
                for (CompletableFuture<Module> task : tasks) {
                    modules.add(task.get());
                }
                return modules;
            } catch (SQLException | NotPossibleException | ExecutionException | InterruptedException e) {
                future.completeExceptionally(e);
            }
            return null;
        }).thenAccept(future::complete);
        return future;
    }

    @Override
    public CompletableFuture<List<Module>> findAll(Connection conn) throws SQLException, NotPossibleException {
        CompletableFuture<List<Module>> future = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return moduleDAO.all(conn);
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
    public CompletableFuture<Void> update(Module obj, Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                moduleDAO.update(obj, conn);
            } catch (SQLException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }

    /**
     * @requires objs!=null /\ objs is in DbContext /\ conn != null /\ conn is not closed
     */
    @Override
    public CompletableFuture<Void> update(Collection<Module> objs, Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                moduleDAO.update(objs, conn);
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
    public CompletableFuture<Void> delete(Module obj, Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                moduleDAO.delete(obj.getCode(), conn);
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
    public CompletableFuture<Void> delete(Collection<Module> objs, Connection conn) throws SQLException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                List<String> ids = objs.stream().map(Module::getCode).collect(Collectors.toList());
                moduleDAO.delete(ids, conn);
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
                moduleDAO.clear(conn);
            } catch (SQLException e) {
                future.completeExceptionally(e);
            }
        }).thenAccept(future::complete);
        return future;
    }
}
