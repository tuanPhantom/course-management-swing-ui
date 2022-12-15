package a2_1901040191.repository.dao;

import a2_1901040191.factory.ModuleFactory;
import a2_1901040191.model.ElectiveModule;
import a2_1901040191.model.Module;
import a2_1901040191.repository.BatchManager;
import a2_1901040191.repository.db.DbConnect;
import a2_1901040191.repository.db.DbSchema;
import a2_1901040191.util.exceptions.InvalidArgumentException;
import a2_1901040191.util.exceptions.NotPossibleException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview Implementation of DAO for Module
 */
public class ModuleDAOImpl implements DAO<Module, String> {
    private Connection conn = DbConnect.getConnection();
    private final BatchManager bm = BatchManager.getInstance();

    public ModuleDAOImpl() {
        try {
            bm.add(CREATE_STMT, conn.prepareStatement(CREATE_STMT));
            bm.add(READ_ONE_STMT, conn.prepareStatement(READ_ONE_STMT));
            bm.add(UPDATE_STMT, conn.prepareStatement(UPDATE_STMT));
            bm.add(DELETE_ONE_STMT, conn.prepareStatement(DELETE_ONE_STMT));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final static String CREATE_STMT = "INSERT INTO " + DbSchema.ModuleTable.NAME + " ("
            + DbSchema.ModuleTable.Cols.CODE + ", "
            + DbSchema.ModuleTable.Cols.NAME + ", "
            + DbSchema.ModuleTable.Cols.SEMESTER + ", "
            + DbSchema.ModuleTable.Cols.CREDITS + ", "
            + DbSchema.ModuleTable.Cols.MODULE_TYPE + ", "
            + DbSchema.ModuleTable.Cols.DEPARTMENT
            + ") VALUES (?, ?, ?, ?, ?, ?)";

    public final static String READ_ONE_STMT = "SELECT * FROM " + DbSchema.ModuleTable.NAME + " WHERE " + DbSchema.ModuleTable.Cols.CODE + " = ?";
    public final static String READ_ALL_STMT = "SELECT * FROM " + DbSchema.ModuleTable.NAME;

    public final static String UPDATE_STMT = "UPDATE " + DbSchema.ModuleTable.NAME + " SET "
            + DbSchema.ModuleTable.Cols.NAME + " = ?, "
            + DbSchema.ModuleTable.Cols.SEMESTER + " = ?, "
            + DbSchema.ModuleTable.Cols.CREDITS + " = ?, "
            + DbSchema.ModuleTable.Cols.MODULE_TYPE + " = ?, "
            + DbSchema.ModuleTable.Cols.DEPARTMENT + " = ? "
            + "WHERE " + DbSchema.ModuleTable.Cols.CODE + " = ?";

    public static final String DELETE_ONE_STMT = "DELETE FROM " + DbSchema.ModuleTable.NAME + " WHERE " + DbSchema.ModuleTable.Cols.CODE + " = ?";
    public static final String DELETE_ALL_STMT = "DELETE FROM " + DbSchema.ModuleTable.NAME;


    @Override
    public void create(Module obj, boolean addToBatch) {
        try {
            PreparedStatement ps = addToBatch ? bm.getBatch(CREATE_STMT) : conn.prepareStatement(CREATE_STMT);
            ps.setString(1, obj.getCode());
            ps.setString(2, obj.getName());
            ps.setInt(3, obj.getSemester());
            ps.setInt(4, obj.getCredits());
            ps.setString(5, obj.getModuleType().toString());
            if (obj.getModuleType().equals(Module.ModuleType.ELECTIVE)) {
                ps.setString(6, ((ElectiveModule) obj).getDepartment());
            } else {
                ps.setNull(6, Types.NVARCHAR);
            }

            if (addToBatch) {
                ps.addBatch();
            } else {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Module read(String key) {
        try {
            PreparedStatement ps = conn.prepareStatement(READ_ONE_STMT);
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString(DbSchema.ModuleTable.Cols.NAME);
                int semester = rs.getInt(DbSchema.ModuleTable.Cols.SEMESTER);
                int credits = rs.getInt(DbSchema.ModuleTable.Cols.CREDITS);
                String mt = rs.getString(DbSchema.ModuleTable.Cols.MODULE_TYPE);
                if (mt.equals(Module.ModuleType.ELECTIVE.toString())) {
                    String department = rs.getString(DbSchema.ModuleTable.Cols.DEPARTMENT);
                    return ModuleFactory.getInstance().createModule(key, name, semester, credits, Module.ModuleType.ELECTIVE, department);
                } else {
                    return ModuleFactory.getInstance().createModule(key, name, semester, credits);
                }
            }
        } catch (SQLException | NotPossibleException | InvalidArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Module> all() {
        List<Module> modules = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(READ_ALL_STMT);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString(DbSchema.ModuleTable.Cols.CODE);
                String name = rs.getString(DbSchema.ModuleTable.Cols.NAME);
                int semester = rs.getInt(DbSchema.ModuleTable.Cols.SEMESTER);
                int credits = rs.getInt(DbSchema.ModuleTable.Cols.CREDITS);
                String mt = rs.getString(DbSchema.ModuleTable.Cols.MODULE_TYPE);
                if (mt.equals(Module.ModuleType.ELECTIVE.toString())) {
                    String department = rs.getString(DbSchema.ModuleTable.Cols.DEPARTMENT);
                    modules.add(ModuleFactory.getInstance().createModule(code, name, semester, credits, Module.ModuleType.ELECTIVE, department));
                } else {
                    modules.add(ModuleFactory.getInstance().createModule(code, name, semester, credits));
                }
            }
        } catch (SQLException | NotPossibleException | InvalidArgumentException e) {
            e.printStackTrace();
        }
        return modules;
    }

    @Override
    public void update(Module obj, boolean addToBatch) {
        try {
            PreparedStatement ps = addToBatch ? bm.getBatch(UPDATE_STMT) : conn.prepareStatement(UPDATE_STMT);
            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getSemester());
            ps.setInt(3, obj.getCredits());
            ps.setString(4, obj.getModuleType().toString());
            if (obj.getModuleType().equals(Module.ModuleType.ELECTIVE)) {
                ps.setString(5, ((ElectiveModule) obj).getDepartment());
            } else {
                ps.setNull(5, Types.NVARCHAR);
            }
            ps.setString(6, obj.getCode());
            if (addToBatch) {
                ps.addBatch();
            } else {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String key, boolean addToBatch) {
        try {
            PreparedStatement ps = addToBatch ? bm.getBatch(DELETE_ONE_STMT) : conn.prepareStatement(DELETE_ONE_STMT);
            ps.setString(1, key);
            if (addToBatch) {
                ps.addBatch();
            } else {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try {
            PreparedStatement ps = conn.prepareStatement(DELETE_ALL_STMT);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect() {
        conn = DbConnect.getConnection();
    }
}
