package a2_1901040191.repository.dao;

import a2_1901040191.model.Student;
import a2_1901040191.repository.BatchManager;
import a2_1901040191.repository.db.DbConnect;
import a2_1901040191.repository.db.DbSchema;
import a2_1901040191.util.exceptions.NotPossibleException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview Implementation of DAO for Student
 */
public class StudentDAOImpl implements DAO<Student, Integer> {
    private Connection conn = DbConnect.getConnection();
    private final BatchManager bm = BatchManager.getInstance();

    public StudentDAOImpl() {
        try {
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
    public void create(Student obj, boolean addToBatch) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student read(Integer key) {
        try {
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
        try {
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
    public void update(Student obj, boolean addToBatch) {
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
