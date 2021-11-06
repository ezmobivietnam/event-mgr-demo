package vn.binhduong.eventmgrdemo.web.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CompareDateValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
/**
 * Ref: https://blog.tericcabrel.com/write-custom-validator-for-body-request-in-spring-boot/
 */
public @interface CompareDate {
    String message() default "{vn.binhduong.eventmgrdemo.web.constraints.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String before();

    String after();
}

