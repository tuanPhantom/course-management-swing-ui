package a2_1901040191.util;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview This annotation class denotes that the constructor or method annotated with this is safe for creating
 * object. In other words, Entity's id is fetched directly from the database instead of generating a new one. By using
 * this method, you may prevent fetching the IDs in the incorrect order.
 */
@Target({CONSTRUCTOR, METHOD})
@Retention(RUNTIME)
public @interface Safe {
}
