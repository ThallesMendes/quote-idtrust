package br.com.idtrust.pricequote.common.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

  private boolean isOptional;
  private String format;

  @Override
  public void initialize(final ValidDate validDate) {
    this.isOptional = validDate.optional();
    this.format = validDate.format();
  }

  @Override
  public boolean isValid(final String value, ConstraintValidatorContext context) {
    boolean validDate = isValidFormat(this.format, value);
    return isOptional || validDate;
  }

  private static boolean isValidFormat(String format, String value) {
    LocalDate date = null;
    try {
      final var dateFormat = DateTimeFormatter.ofPattern(format);
      if (value != null){
        date = LocalDate.parse(value, dateFormat);
        if (!value.equals(dateFormat.format(date))) {
          date = null;
        }
      }

    } catch (Exception ex) {
      return false;
    }
    return date != null;
  }
}
