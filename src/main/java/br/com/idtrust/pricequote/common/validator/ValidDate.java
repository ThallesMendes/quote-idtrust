package br.com.idtrust.pricequote.common.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidDateValidator.class)
public @interface ValidDate {

  String message() default "invalid date";
  String format() default "yyyy-MM-dd";
  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean optional() default false;

}
