package br.com.idtrust.pricequote.service.awesome;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import br.com.idtrust.pricequote.common.exception.EntityNotFoundException;
import br.com.idtrust.pricequote.common.exception.ServiceException;
import br.com.idtrust.pricequote.common.service.BaseServiceTest;
import br.com.idtrust.pricequote.seed.AwesomeResponseSeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AwesomeServiceTest extends BaseServiceTest {

  private AwesomeService awesomeService;

  @BeforeEach
  void setUp() {
    this.awesomeService = new AwesomeService("http://localhost");
  }

  @Test
  @DisplayName("should be get currency quote success")
  void shouldBeGetCurrencyQuoteSuccess() {

    this.awesomeService.setClient(client(HttpStatus.OK.value(), "Success", AwesomeResponseSeeder.simple(5.214), true));

    final var response = this.awesomeService.getCurrencyQuote("USD", "BRL", LocalDate.now(), LocalDate.now());

    assertTrue(response.isPresent());
  }

  @Test
  @DisplayName("should be get currency quote with error max attempts")
  void shouldBeGetCurrencyQuoteErrorMaxAttempts() {

    this.awesomeService.setClient(client(HttpStatus.BAD_REQUEST.value(), "Bad Request", AwesomeResponseSeeder.simple(5.214), true));

    assertThrows(ServiceException.class, () ->
        this.awesomeService.getCurrencyQuote("USD", "BRL", LocalDate.now(), LocalDate.now())
    );
  }

  @Test
  @DisplayName("should be get value currency quote success")
  void shouldBeGetValueCurrencyQuoteSuccess() {

    this.awesomeService.setClient(client(HttpStatus.OK.value(), "Success", AwesomeResponseSeeder.simple(5.214), true));

    final var value = this.awesomeService.getValueCurrencyQuote("USD", "BRL", LocalDate.now(), LocalDate.now());

    assertEquals(5.214, value);

  }

  @Test
  @DisplayName("should be get value currency quote error when value ask is null")
  void shouldBeGetValueCurrencyQuoteErrorWhenValueAskIsNull() {

    this.awesomeService.setClient(client(HttpStatus.OK.value(), "Success", AwesomeResponseSeeder.simple(null), true));

    assertThrows(EntityNotFoundException.class, () ->
        this.awesomeService.getValueCurrencyQuote("USD", "BRL", LocalDate.now(), LocalDate.now())
    );

  }

}