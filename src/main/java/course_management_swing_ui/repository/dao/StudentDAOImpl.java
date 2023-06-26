package course_management_swing_ui.repository.dao;

import course_management_swing_ui.model.Student;
import course_management_swing_ui.repository.BatchManager;
import course_management_swing_ui.repository.db.DbConnect;
import course_management_swing_ui.repository.db.DbSchema;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.1
 * @Overview Implementation of DAO for Student
 */
public class StudentDAOImpl implements DAO<Student, Integer> {
    private final BatchManager bm = BatchManager.getInstance();

    public StudentDAOImpl() {
        try {
            Connection conn = BatchManager.getBatchConnection();
            bm.add(CREATE_STMT, conn.prepareStatement(CREATE_STMT));
            bm.add(READ_ONE_STMT, conn.prepareStatement(READ_ONE_STMT));
            bm.add(UPDATE_STMT, conn.prepareStatement(UPDATE_STMT));
            bm.add(DELETE_ONE_STMT, conn.prepareStatement(DELETE_ONE_STMT));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final static String CREATE_STMT = "INSERT INTO " + DbSchema.StudentTable.NAME + " ("
            + DbSchema.StudentTable.Cols.ID + ", "
            + DbSchema.StudentTable.Cols.NAME + ", "
            + DbSchema.StudentTable.Cols.DOB + ", "
            + DbSchema.StudentTable.Cols.ADDRESS + ", "
            + DbSchema.StudentTable.Cols.EMAIL
            + ") VALUES (?, ?, ?, ?, ?)";

    public final static String READ_ONE_STMT = "SELECT * FROM " + DbSchema.StudentTable.NAME + " WHERE " + DbSchema.StudentTable.Cols.ID + " = ?";
    public final static String READ_ALL_STMT = "SELECT * FROM " + DbSchema.StudentTable.NAME;

    public final static String UPDATE_STMT = "UPDATE " + DbSchema.StudentTable.NAME + " SET "
            + DbSchema.StudentTable.Cols.NAME + " = ?, "
            + DbSchema.StudentTable.Cols.DOB + " = ?, "
            + DbSchema.StudentTable.Cols.ADDRESS + " = ?, "
            + DbSchema.StudentTable.Cols.EMAIL + " = ? "
            + "WHERE " + DbSchema.StudentTable.Cols.ID + " = ?";

    public static final String DELETE_ONE_STMT = "DELETE FROM " + DbSchema.StudentTable.NAME + " WHERE " + DbSchema.StudentTable.Cols.ID + " = ?";
    public static final String DELETE_ALL_STMT = "DELETE FROM " + DbSchema.StudentTable.NAME;

    @Override
    public boolean create(Student obj, boolean addToBatch) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                PreparedStatement ps = addToBatch ? bm.getBatch(CREATE_STMT) : conn.prepareStatement(CREATE_STMT);
                ps.setInt(1, obj.getNumericalId());
                ps.setString(2, obj.getName());
                ps.setString(3, obj.getDob().toString());
                ps.setString(4, obj.getAddress());
                ps.setString(5, obj.getEmail());
                if (addToBatch) {
                    ps.addBatch();
                } else {
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Student read(Integer key) {
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(READ_ONE_STMT);
            ps.setInt(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString(DbSchema.StudentTable.Cols.NAME);
                String dob = rs.getString(DbSchema.StudentTable.Cols.DOB);
                String address = rs.getString(DbSchema.StudentTable.Cols.ADDRESS);
                String email = rs.getString(DbSchema.StudentTable.Cols.EMAIL);
                return new Student(key, name, LocalDate.parse(dob), address, email);
            }
        } catch (SQLException | NotPossibleException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> all() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(READ_ALL_STMT);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(DbSchema.StudentTable.Cols.ID);
                String name = rs.getString(DbSchema.StudentTable.Cols.NAME);
                String dob = rs.getString(DbSchema.StudentTable.Cols.DOB);
                String address = rs.getString(DbSchema.StudentTable.Cols.ADDRESS);
                String email = rs.getString(DbSchema.StudentTable.Cols.EMAIL);
                students.add(new Student(id, name, LocalDate.parse(dob), address, email));
            }
        } catch (SQLException | NotPossibleException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public boolean update(Student obj, boolean addToBatch) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                PreparedStatement ps = addToBatch ? bm.getBatch(UPDATE_STMT) : conn.prepareStatement(UPDATE_STMT);
                ps.setString(1, obj.getName());
                ps.setString(2, obj.getDob().toString());
                ps.setString(3, obj.getAddress());
                ps.setString(4, obj.getEmail());
                ps.setInt(5, obj.getNumericalId());
                if (addToBatch) {
                    ps.addBatch();
                } else {
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer key, boolean addToBatch) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                PreparedStatement ps = addToBatch ? bm.getBatch(DELETE_ONE_STMT) : conn.prepareStatement(DELETE_ONE_STMT);
                ps.setInt(1, key);
                if (addToBatch) {
                    ps.addBatch();
                } else {
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean clear() {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                PreparedStatement ps = conn.prepareStatement(DELETE_ALL_STMT);
                ps.executeUpdate();
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
