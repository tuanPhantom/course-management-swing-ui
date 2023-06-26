package course_management_swing_ui.util.dto;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This annotation class denotes that the field annotated with this will be displayed in the JTable and be
 * recognized by DtoGenerator
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface DisplayInTable {
    boolean skip() default false;
}
