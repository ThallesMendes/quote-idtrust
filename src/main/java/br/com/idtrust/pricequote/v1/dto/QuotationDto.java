package br.com.idtrust.pricequote.v1.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationDto implements Serializable {
  @Serial
  private static final long serialVersionUID = -4001763308315931337L;

  private String culture;
  private BigDecimal value;
  private LocalDate date;

}
