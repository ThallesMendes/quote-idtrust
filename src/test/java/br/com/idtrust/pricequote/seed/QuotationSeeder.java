package br.com.idtrust.pricequote.seed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import br.com.idtrust.pricequote.model.Quotation;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuotationSeeder {

  public Quotation simple() {
    return Quotation.builder()
        .uuid(UUID.randomUUID())
        .culture("CULTURE")
        .date(LocalDate.now())
        .originalValue(BigDecimal.valueOf(10.00))
        .value(BigDecimal.valueOf(50.00))
        .currencyQuotation(BigDecimal.valueOf(5.00))
        .build();
  }
}
