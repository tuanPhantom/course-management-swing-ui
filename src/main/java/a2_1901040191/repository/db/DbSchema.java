package a2_1901040191.repository.db;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview a class that provides details about the database's tables and each column within every table
 */
public class DbSchema {
    public static final class StudentTable {
        public static final String NAME = "student";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String DOB = "dob";
            public static final String ADDRESS = "address";
            public static final String EMAIL = "email";
        }
    }

    public static final class ModuleTable {
        public static final String NAME = "module";

        public static final class Cols {
            public static final String CODE = "code";
            public static final String NAME = "name";
            public static final String SEMESTER = "semester";
            public static final String CREDITS = "credits";
            public static final String MODULE_TYPE = "module_type";
            public static final String DEPARTMENT = "department";
        }
    }

    public static final class EnrollmentTable {
        public static final String NAME = "enrollment";

        public static final class Cols {
            public static final String ID = "id";
            public static final String STUDENT_ID = "student_id";
            public static final String MODULE_CODE = "module_code";
            public static final String INTERNAL_MARK = "internal_mark";
            public static final String EXAMINATION_MARK = "examination_mark";
        }
    }
}
