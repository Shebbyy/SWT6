package swt6.spring.basics.ioc.util;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Target({ElementType.FIELD,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.TYPE,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    Type value() default Type.CONSOLE;
    enum Type {CONSOLE, FILE}
}
