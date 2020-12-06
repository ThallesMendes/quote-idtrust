package br.com.idtrust.pricequote.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {

  public String formatDate(final LocalDate localDate, final String format) {
    final var formatter = DateTimeFormatter.ofPattern(format);
    return localDate.format(formatter);
  }

}
