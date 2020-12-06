package br.com.idtrust.pricequote.service.quandl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;

import br.com.idtrust.pricequote.common.exception.EntityNotFoundException;
import br.com.idtrust.pricequote.common.exception.ServiceException;
import br.com.idtrust.pricequote.common.service.BaseServiceTest;
import br.com.idtrust.pricequote.seed.QuandlResponseSeeder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class QuandlServiceTest extends BaseServiceTest {

  private QuandlService quandlService;

  @BeforeEach
  void setUp() {
    this.quandlService = new QuandlService("http:\\localhost", "key");
  }

  @Test
  @DisplayName("should be get quote success")
  void shouldBeGetQuoteSuccess() {

    this.quandlService.setClient(client(HttpStatus.OK.value(), "Success", QuandlResponseSeeder.simple(250.00), false));

    final var response = this.quandlService.getQuote("CULT", LocalDate.now(), LocalDate.now());

    Assertions.assertTrue(response.isPresent());
  }

  @Test
  @DisplayName("should be get quote error")
  void shouldBeGetQuoteError() {

    this.quandlService.setClient(client(HttpStatus.BAD_REQUEST.value(), "Bad Request", QuandlResponseSeeder.simple(250.00), false));

    assertThrows(ServiceException.class, () ->
        this.quandlService.getQuote("CULT", LocalDate.now(), LocalDate.now())
    );

  }

  @Test
  @DisplayName("should be get quote value success")
  void shouldBeGetQuoteValueSuccess() {

    this.quandlService.setClient(client(HttpStatus.OK.value(), "Success", QuandlResponseSeeder.simple(250.00), false));

    final var value = this.quandlService.getQuoteValue("CULT", LocalDate.now(), LocalDate.now());

    assertEquals(250.00, value);

  }

  @Test
  @DisplayName("should be get quote value error when data is empty")
  void shouldBeGetQuoteValueErrorWhenDataIsEmpty() {

    final var simple = QuandlResponseSeeder.simple(250.00);
    simple.getDataset().setData(Collections.emptyList());
    this.quandlService.setClient(client(HttpStatus.OK.value(), "Success", simple, false));

    assertThrows(EntityNotFoundException.class, () ->
        this.quandlService.getQuoteValue("CULT", LocalDate.now(), LocalDate.now())
    );

  }
}