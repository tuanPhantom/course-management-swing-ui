package course_management_swing_ui.repositories.dao;

import course_management_swing_ui.models.Enrollment;
import course_management_swing_ui.repositories.db.DbSchema;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.2
 * @Overview Implementation of DAO for Enrollment
 */
public class EnrollmentDAOImpl implements DAO<Object, Integer> {
    public final static String CREATE_STMT = "INSERT INTO " + DbSchema.EnrollmentTable.NAME + " ("
            + DbSchema.EnrollmentTable.Cols.ID + ", "
            + DbSchema.EnrollmentTable.Cols.STUDENT_ID + ", "
            + DbSchema.EnrollmentTable.Cols.MODULE_CODE + ", "
            + DbSchema.EnrollmentTable.Cols.INTERNAL_MARK + ", "
            + DbSchema.EnrollmentTable.Cols.EXAMINATION_MARK
            + ") VALUES (?, ?, ?, ?, ?)";

    public final static String READ_ONE_STMT = "SELECT * FROM " + DbSchema.EnrollmentTable.NAME + " WHERE " + DbSchema.EnrollmentTable.Cols.ID + " = ?";
    public final static String READ_ALL_STMT = "SELECT * FROM " + DbSchema.EnrollmentTable.NAME;

    public final static String UPDATE_STMT = "UPDATE " + DbSchema.EnrollmentTable.NAME + " SET "
            + DbSchema.EnrollmentTable.Cols.STUDENT_ID + " = ?, "
            + DbSchema.EnrollmentTable.Cols.MODULE_CODE + " = ?, "
            + DbSchema.EnrollmentTable.Cols.INTERNAL_MARK + " = ?, "
            + DbSchema.EnrollmentTable.Cols.EXAMINATION_MARK + " = ?"
            + "WHERE " + DbSchema.EnrollmentTable.Cols.ID + " = ?";

    public static final String DELETE_ONE_STMT = "DELETE FROM " + DbSchema.EnrollmentTable.NAME + " WHERE " + DbSchema.EnrollmentTable.Cols.ID + " = ?";
    public static final String DELETE_ALL_STMT = "DELETE FROM " + DbSchema.EnrollmentTable.NAME;

    @Override
    public void create(Object obj, Connection conn) throws SQLException {
        Enrollment e = (Enrollment) obj;
        PreparedStatement ps = conn.prepareStatement(CREATE_STMT);
        ps.setInt(1, e.getId());
        ps.setInt(2, e.getStudent().getNumericalId());
        ps.setString(3, e.getModule().getCode());
        ps.setDouble(4, e.getInternalMark());
        ps.setDouble(5, e.getExaminationMark());
        if (ps.executeUpdate() != 1) throw new SQLException("query failed!");
    }

    @Override
    public void create(Collection<Object> objs, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(CREATE_STMT);
        for (Object obj : objs) {
            Enrollment e = (Enrollment) obj;
            ps.setInt(1, e.getId());
            ps.setInt(2, e.getStudent().getNumericalId());
            ps.setString(3, e.getModule().getCode());
            ps.setDouble(4, e.getInternalMark());
            ps.setDouble(5, e.getExaminationMark());
            ps.addBatch();
        }
        if (ps.executeBatch().length != objs.size()) throw new SQLException("query failed!");
    }

    @Override
    public List<?> read(Integer key, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(READ_ONE_STMT);
        ps.setInt(1, key);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(DbSchema.EnrollmentTable.Cols.ID);
            int student_id = rs.getInt(DbSchema.EnrollmentTable.Cols.STUDENT_ID);
            String module_code = rs.getString(DbSchema.EnrollmentTable.Cols.MODULE_CODE);
            double im = rs.getDouble(DbSchema.EnrollmentTable.Cols.INTERNAL_MARK);
            double em = rs.getDouble(DbSchema.EnrollmentTable.Cols.EXAMINATION_MARK);
            return List.of(id, student_id, module_code, im, em);
        }
        return null;
    }

    @Override
    public List<Object> read(Collection<Integer> keys, Connection conn) throws SQLException {
        List<Object> enrollments = new ArrayList<>();
        for (Integer key : keys) {
            PreparedStatement ps = conn.prepareStatement(READ_ONE_STMT);
            ps.setInt(1, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(DbSchema.EnrollmentTable.Cols.ID);
                int student_id = rs.getInt(DbSchema.EnrollmentTable.Cols.STUDENT_ID);
                String module_code = rs.getString(DbSchema.EnrollmentTable.Cols.MODULE_CODE);
                double im = rs.getDouble(DbSchema.EnrollmentTable.Cols.INTERNAL_MARK);
                double em = rs.getDouble(DbSchema.EnrollmentTable.Cols.EXAMINATION_MARK);
                enrollments.add(List.of(id, student_id, module_code, im, em));
            }
        }
        return enrollments;
    }

    @Override
    public List<Object> all(Connection conn) throws SQLException {
        List<Object> enrollments = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(READ_ALL_STMT);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt(DbSchema.EnrollmentTable.Cols.ID);
            int student_id = rs.getInt(DbSchema.EnrollmentTable.Cols.STUDENT_ID);
            String module_code = rs.getString(DbSchema.EnrollmentTable.Cols.MODULE_CODE);
            double im = rs.getDouble(DbSchema.EnrollmentTable.Cols.INTERNAL_MARK);
            double em = rs.getDouble(DbSchema.EnrollmentTable.Cols.EXAMINATION_MARK);
            enrollments.add(List.of(id, student_id, module_code, im, em));
        }
        return enrollments;
    }

    @Override
    public void update(Object obj, Connection conn) throws SQLException {
        Enrollment e = (Enrollment) obj;
        PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);
        ps.setInt(1, e.getStudent().getNumericalId());
        ps.setString(2, e.getModule().getCode());
        ps.setDouble(3, e.getInternalMark());
        ps.setDouble(4, e.getExaminationMark());
        ps.setInt(5, e.getId());
        if (ps.executeUpdate() != 1) throw new SQLException("query failed!");
    }

    @Override
    public void update(Collection<Object> objs, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);
        for (Object obj : objs) {
            Enrollment e = (Enrollment) obj;
            ps.setInt(1, e.getStudent().getNumericalId());
            ps.setString(2, e.getModule().getCode());
            ps.setDouble(3, e.getInternalMark());
            ps.setDouble(4, e.getExaminationMark());
            ps.setInt(5, e.getId());
            ps.addBatch();
        }
        if (ps.executeBatch().length != objs.size()) throw new SQLException("query failed!");
    }

    @Override
    public void delete(Integer key, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(DELETE_ONE_STMT);
        ps.setInt(1, key);
        if (ps.executeUpdate() != 1) throw new SQLException("query failed!");
    }

    @Override
    public void delete(Collection<Integer> keys, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(DELETE_ONE_STMT);
        for (Integer key : keys) {
            ps.setInt(1, key);
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
