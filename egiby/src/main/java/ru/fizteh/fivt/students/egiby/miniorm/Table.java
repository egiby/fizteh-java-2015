package ru.fizteh.fivt.students.egiby.miniorm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by egiby on 18.12.15.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name() default "";
}
