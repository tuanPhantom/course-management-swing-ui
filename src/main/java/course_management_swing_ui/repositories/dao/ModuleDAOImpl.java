package course_management_swing_ui.repositories.dao;

import course_management_swing_ui.factories.ModuleFactory;
import course_management_swing_ui.models.ElectiveModule;
import course_management_swing_ui.models.Module;
import course_management_swing_ui.repositories.db.DbSchema;
import course_management_swing_ui.util.exceptions.InvalidArgumentException;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.2
 * @Overview Implementation of DAO for Module
 */
public class ModuleDAOImpl implements DAO<Module, String> {
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
    public void create(Module obj, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(CREATE_STMT);
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
        if (ps.executeUpdate() != 1) throw new SQLException("query failed!");
    }

    @Override
    public void create(Collection<Module> objs, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(CREATE_STMT);
        for (Module obj : objs) {
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
            ps.addBatch();
        }
        if (ps.executeBatch().length != objs.size()) throw new SQLException("query failed!");
    }

    @Override
    public Module read(String key, Connection conn) throws SQLException, NotPossibleException {
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
                try {
                    return ModuleFactory.getInstance().createModule(key, name, semester, credits, Module.ModuleType.ELECTIVE, department);
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
            } else {
                return ModuleFactory.getInstance().createModule(key, name, semester, credits);
            }
        }
        return null;
    }

    @Override
    public List<Module> read(Collection<String> keys, Connection conn) throws SQLException, NotPossibleException {
        List<Module> modules = new ArrayList<>();
        for (String key : keys) {
            PreparedStatement ps = conn.prepareStatement(READ_ONE_STMT);
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString(DbSchema.ModuleTable.Cols.CODE);
                String name = rs.getString(DbSchema.ModuleTable.Cols.NAME);
                int semester = rs.getInt(DbSchema.ModuleTable.Cols.SEMESTER);
                int credits = rs.getInt(DbSchema.ModuleTable.Cols.CREDITS);
                String mt = rs.getString(DbSchema.ModuleTable.Cols.MODULE_TYPE);
                if (mt.equals(Module.ModuleType.ELECTIVE.toString())) {
                    String department = rs.getString(DbSchema.ModuleTable.Cols.DEPARTMENT);
                    try {
                        modules.add(ModuleFactory.getInstance().createModule(code, name, semester, credits, Module.ModuleType.ELECTIVE, department));
                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    modules.add(ModuleFactory.getInstance().createModule(code, name, semester, credits));
                }
            }
        }
        return modules;
    }

    @Override
    public List<Module> all(Connection conn) throws SQLException, NotPossibleException {
        List<Module> modules = new ArrayList<>();
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
                try {
                    modules.add(ModuleFactory.getInstance().createModule(code, name, semester, credits, Module.ModuleType.ELECTIVE, department));
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                }
            } else {
                modules.add(ModuleFactory.getInstance().createModule(code, name, semester, credits));
            }
        }
        return modules;
    }

    @Override
    public void update(Module obj, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);
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
        if (ps.executeUpdate() != 1) throw new SQLException("query failed!");
    }

    @Override
    public void update(Collection<Module> objs, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);
        for (Module obj : objs) {
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
            ps.addBatch();
        }
        if (ps.executeBatch().length != objs.size()) throw new SQLException("query failed!");
    }

    @Override
    public void delete(String key, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(DELETE_ONE_STMT);
        ps.setString(1, key);
        if (ps.executeUpdate() != 1) throw new SQLException("query failed!");
    }

    @Override
    public void delete(Collection<String> keys, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(DELETE_ONE_STMT);
        for (String key : keys) {
            ps.setString(1, key);
            ps.addBatch();
        }
        if (ps.executeBatch().length != keys.size()) throw new SQLException("query failed!");
    }

    @Override
    public void clear(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(DELETE_ALL_STMT);
        ps.executeUpdate();
    }
}
