package a2_1901040191.repository;

import a2_1901040191.model.Enrollment;
import a2_1901040191.model.Module;
import a2_1901040191.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview A Db context is similar to persistence context, which is a set of entity instances in which for any
 * persistent entity identity there is a unique entity instance. Within the persistence context, the entity instances
 * and their lifecycle are managed. The EntityManager API is used to create and remove persistent entity instances, to
 * find entities by their primary key, and to query over entities.
 */
public interface DbContext {
    List<Enrollment> enrollmentDbContext = new ArrayList<>();
    List<Student> studentDbContext = new ArrayList<>();
    List<Module> moduleDbContext = new ArrayList<>();
}
