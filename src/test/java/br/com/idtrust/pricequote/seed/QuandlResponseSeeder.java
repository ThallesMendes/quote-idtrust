package br.com.idtrust.pricequote.seed;

import java.util.Collections;
import java.util.List;

import br.com.idtrust.pricequote.service.quandl.QuandlResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuandlResponseSeeder {

  public QuandlResponse simple(Double value) {
    return QuandlResponse.builder()
        .dataset(QuandlResponse.Dataset.builder()
            .id(1L)
            .data(Collections.singletonList(
                List.of("1", value)
            ))
            .build())
        .build();
  }

}
