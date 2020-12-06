package br.com.idtrust.pricequote.seed;

import br.com.idtrust.pricequote.service.awesome.AwesomeResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AwesomeResponseSeeder {

  public AwesomeResponse simple(final Double value) {
    return AwesomeResponse.builder()
        .code("USD")
        .codeIn("BRL")
        .ask(value)
        .build();
  }

}
