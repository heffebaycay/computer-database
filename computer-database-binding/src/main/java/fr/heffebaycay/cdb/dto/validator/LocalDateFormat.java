package fr.heffebaycay.cdb.dto.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation to validate String objects to a locale-specific date pattern
 *
 */
@Documented
@Constraint(validatedBy = {LocalDateFormatConstraintValidator.class})
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface LocalDateFormat {

  String message() default "{LocalDateFormat}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
}
