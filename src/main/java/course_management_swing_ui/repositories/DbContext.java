package course_management_swing_ui.repositories;

import course_management_swing_ui.models.Enrollment;
import course_management_swing_ui.models.Module;
import course_management_swing_ui.models.Student;

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
