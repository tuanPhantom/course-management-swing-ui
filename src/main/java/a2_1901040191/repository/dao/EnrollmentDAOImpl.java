package a2_1901040191.repository.dao;

import a2_1901040191.model.Enrollment;
import a2_1901040191.repository.BatchManager;
import a2_1901040191.repository.db.DbConnect;
import a2_1901040191.repository.db.DbSchema;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview Implementation of DAO for Enrollment
 */
public class EnrollmentDAOImpl implements DAO<Object, Integer> {
    private Connection conn = DbConnect.getConnection();
    private final BatchManager bm = BatchManager.getInstance();

    public EnrollmentDAOImpl() {
        try {
            bm.add(CREATE_STMT, conn.prepareStatement(CREATE_STMT));
            bm.add(READ_ONE_STMT, conn.prepareStatement(READ_ONE_STMT));
            bm.add(UPDATE_STMT, conn.prepareStatement(UPDATE_STMT));
            bm.add(DELETE_ONE_STMT, conn.prepareStatement(DELETE_ONE_STMT));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
    public void create(Object obj, boolean addToBatch) {
        try {
            Enrollment e = (Enrollment) obj;
            PreparedStatement ps = addToBatch ? bm.getBatch(CREATE_STMT) : conn.prepareStatement(CREATE_STMT);
            ps.setInt(1, e.getId());
            ps.setInt(2, e.getStudent().getNumericalId());
            ps.setString(3, e.getModule().getCode());
            ps.setDouble(4, e.getInternalMark());
            ps.setDouble(5, e.getExaminationMark());
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
    public List<?> read(Integer key) {
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Object> all() {
        List<Object> enrollments = new ArrayList<>();
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public void update(Object obj, boolean addToBatch) {
        try {
            Enrollment e = (Enrollment) obj;
            PreparedStatement ps = addToBatch ? bm.getBatch(UPDATE_STMT) : conn.prepareStatement(UPDATE_STMT);
            ps.setInt(1, e.getStudent().getNumericalId());
            ps.setString(2, e.getModule().getCode());
            ps.setDouble(3, e.getInternalMark());
            ps.setDouble(4, e.getExaminationMark());
            ps.setInt(5, e.getId());
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
    public void delete(Integer key, boolean addToBatch) {
        try {
            PreparedStatement ps = addToBatch ? bm.getBatch(DELETE_ONE_STMT) : conn.prepareStatement(DELETE_ONE_STMT);
            ps.setInt(1, key);
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
