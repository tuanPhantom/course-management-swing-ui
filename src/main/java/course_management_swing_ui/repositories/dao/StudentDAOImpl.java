package course_management_swing_ui.repositories.dao;

import course_management_swing_ui.models.Student;
import course_management_swing_ui.repositories.db.DbSchema;
import course_management_swing_ui.util.exceptions.NotPossibleException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.2
 * @Overview Implementation of DAO for Student
 */
public class StudentDAOImpl implements DAO<Student, Integer> {
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
    public void create(Student obj, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(CREATE_STMT);
        ps.setInt(1, obj.getNumericalId());
        ps.setString(2, obj.getName());
        ps.setString(3, obj.getDob().toString());
        ps.setString(4, obj.getAddress());
        ps.setString(5, obj.getEmail());
        if (ps.executeUpdate() != 1) throw new SQLException("query failed!");
    }

    @Override
    public void create(Collection<Student> objs, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(CREATE_STMT);
        for (Student obj : objs) {
            ps.setInt(1, obj.getNumericalId());
            ps.setString(2, obj.getName());
            ps.setString(3, obj.getDob().toString());
            ps.setString(4, obj.getAddress());
            ps.setString(5, obj.getEmail());
            ps.addBatch();
        }
        if (ps.executeBatch().length != objs.size()) throw new SQLException("query failed!");
    }

    @Override
    public Student read(Integer key, Connection conn) throws SQLException, NotPossibleException {
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
        return null;
    }

    @Override
    public List<Student> read(Collection<Integer> keys, Connection conn) {
        List<Student> students = new ArrayList<>();
        try {
            for (Integer key : keys) {
                PreparedStatement ps = conn.prepareStatement(READ_ONE_STMT);
                ps.setInt(1, key);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt(DbSchema.StudentTable.Cols.ID);
                    String name = rs.getString(DbSchema.StudentTable.Cols.NAME);
                    String dob = rs.getString(DbSchema.StudentTable.Cols.DOB);
                    String address = rs.getString(DbSchema.StudentTable.Cols.ADDRESS);
                    String email = rs.getString(DbSchema.StudentTable.Cols.EMAIL);
                    students.add(new Student(id, name, LocalDate.parse(dob), address, email));
                }
            }
        } catch (SQLException | NotPossibleException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public List<Student> all(Connection conn) throws SQLException, NotPossibleException {
        List<Student> students = new ArrayList<>();
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
        return students;
    }

    @Override
    public void update(Student obj, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);
        ps.setString(1, obj.getName());
        ps.setString(2, obj.getDob().toString());
        ps.setString(3, obj.getAddress());
        ps.setString(4, obj.getEmail());
        ps.setInt(5, obj.getNumericalId());
        if (ps.executeUpdate() != 1) throw new SQLException("query failed!");
    }

    @Override
    public void update(Collection<Student> objs, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);
        for (Student obj : objs) {
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getDob().toString());
            ps.setString(3, obj.getAddress());
            ps.setString(4, obj.getEmail());
            ps.setInt(5, obj.getNumericalId());
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
