package course_management_swing_ui.services;

import course_management_swing_ui.models.Enrollment;
import course_management_swing_ui.models.Student;
import course_management_swing_ui.repositories.DbContext;
import course_management_swing_ui.repositories.EnrollmentRepository;
import course_management_swing_ui.repositories.StudentRepository;
import course_management_swing_ui.repositories.db.DbConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Phan Quang Tuan
 * @version 1.3
 * @Overview Implementation of Service for Student.
 */
public class StudentService implements Service<Student, Integer> {
    private final StudentRepository studentRepository = new StudentRepository();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();

    /**
     * add Object to the Database
     * @param obj
     * @requires obj != null
     */
    @Override
    public void add(Student obj) {
        try (Connection conn = DbConnect.getConnection()) {
            studentRepository.add(obj, conn).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * add all Objects in objs to the Database
     * @param objs
     * @requires objs != null
     */
    @Override
    public void addAll(Collection<Student> objs) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                studentRepository.addAll(objs, conn).get();
                conn.commit();
            } catch (Exception e) {
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
    public Student findById(Integer id) {
        try (Connection conn = DbConnect.getConnection()) {
            return studentRepository.findById(id, conn).get();
        } catch (Exception e) {
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
    public List<Student> findById(Collection<Integer> ids) {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DbConnect.getConnection()) {
            students.addAll(studentRepository.findById(ids, conn).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * return a list of all objects of T
     * @requires ids != null
     */
    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DbConnect.getConnection()) {
            students.addAll(studentRepository.findAll(conn).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * update the row that share the primary key with obj
     * @param obj
     * @requires obj != null
     */
    @Override
    public void update(Student obj) {
        try (Connection conn = DbConnect.getConnection()) {
            studentRepository.update(obj, conn).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * update all rows that share the primary key with each obj in objs
     * @param objs
     * @requires obj != null
     */
    @Override
    public void update(Collection<Student> objs) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                studentRepository.update(objs, conn).get();
                conn.commit();
            } catch (Exception e) {
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
    public void delete(Student obj) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                List<CompletableFuture<Void>> tasks = new ArrayList<>();
                CompletableFuture<Void> studentTask = studentRepository.delete(obj, conn);
                tasks.add(studentTask);
                Optional<Enrollment> opt = DbContext.enrollmentDbContext.stream().filter(e -> e.getStudent().getNumericalId() == obj.getNumericalId()).findFirst();
                if (opt.isPresent()) {
                    Enrollment enrollment = opt.get();
                    CompletableFuture<Void> enTask = enrollmentRepository.delete(enrollment, conn);
                    tasks.add(enTask);
                }
                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).get();
                conn.commit();
            } catch (Exception e) {
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
    public void delete(Collection<Student> objs) {
        try (Connection conn = DbConnect.getConnection()) {
            try {
                conn.setAutoCommit(false);
                CompletableFuture<Void> studentTask = studentRepository.delete(objs, conn);

                // delete enrollments
                List<Integer> numId = objs.stream().map(Student::getNumericalId).collect(Collectors.toList());
                List<Enrollment> enrollments = DbContext.enrollmentDbContext.stream().filter(e -> numId.contains(e.getStudent().getNumericalId())).collect(Collectors.toList());
                CompletableFuture<Void> enTask = enrollmentRepository.delete(enrollments, conn);
                CompletableFuture.allOf(studentTask, enTask).get();
                conn.commit();
            } catch (Exception e) {
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
                CompletableFuture<Void> studentTask = studentRepository.deleteAll(conn);
                CompletableFuture<Void> enTask = enrollmentRepository.deleteAll(conn);
                CompletableFuture.allOf(studentTask, enTask).get();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
