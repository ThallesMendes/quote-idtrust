package br.com.idtrust.pricequote.v1.mapper;

import br.com.idtrust.pricequote.model.Quotation;
import br.com.idtrust.pricequote.v1.dto.QuotationDto;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuotationMapper {

  public QuotationDto toResponseDto(@NonNull final Quotation quotation) {
    return QuotationDto.builder()
        .culture(quotation.getCulture())
        .date(quotation.getDate())
        .value(quotation.getValue())
        .build();
  }

}
