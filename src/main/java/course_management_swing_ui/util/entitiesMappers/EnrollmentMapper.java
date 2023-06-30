package course_management_swing_ui.util.entitiesMappers;

import course_management_swing_ui.models.Enrollment;
import course_management_swing_ui.models.Module;
import course_management_swing_ui.models.Student;
import course_management_swing_ui.repositories.DbContext;

public class EnrollmentMapper {
    private static EnrollmentMapper instance;
    private final EntitiesMapper entitiesMapper = EntitiesMapper.getInstance();

    private EnrollmentMapper() {

    }

    public static EnrollmentMapper getInstance() {
        if (instance == null) {
            instance = new EnrollmentMapper();
        }
        return instance;
    }

    public void mapStudent() {
        entitiesMapper.<Enrollment, Student, Integer>mapDependency(DbContext.enrollmentDbContext, Enrollment::getStudent, Enrollment::setStudent, DbContext.studentDbContext, Student::getNumericalId);
    }

    public void mapModule() {
        entitiesMapper.<Enrollment, Module, String>mapDependency(DbContext.enrollmentDbContext, Enrollment::getModule, Enrollment::setModule, DbContext.moduleDbContext, Module::getCode);
    }
}
