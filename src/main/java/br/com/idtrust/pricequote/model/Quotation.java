package br.com.idtrust.pricequote.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "uuid")
@Builder
@Entity
@Table(schema = "public", name = "quotation")
public class Quotation implements Serializable {
  @Serial
  private static final long serialVersionUID = 470025236429744785L;

  @Id
  @Column(columnDefinition = "uuid")
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type="pg-uuid")
  private UUID uuid;

  private String culture;

  private LocalDate date;

  private BigDecimal value;

  private BigDecimal originalValue;

  private BigDecimal currencyQuotation;
}
