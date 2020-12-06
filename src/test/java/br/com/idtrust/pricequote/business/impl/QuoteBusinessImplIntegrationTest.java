package br.com.idtrust.pricequote.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import javax.transaction.Transactional;

import br.com.idtrust.pricequote.business.QuoteBusiness;
import br.com.idtrust.pricequote.common.exception.ServiceException;
import br.com.idtrust.pricequote.common.service.BaseServiceTest;
import br.com.idtrust.pricequote.common.service.BaseServiceTest.FakeInterceptor;
import br.com.idtrust.pricequote.model.Quotation;
import br.com.idtrust.pricequote.repository.QuotationRepository;
import br.com.idtrust.pricequote.seed.AwesomeResponseSeeder;
import br.com.idtrust.pricequote.seed.QuandlResponseSeeder;
import br.com.idtrust.pricequote.service.awesome.AwesomeService;
import br.com.idtrust.pricequote.service.quandl.QuandlService;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles({"test"})
class QuoteBusinessImplIntegrationTest {

  @Autowired
  private QuoteBusiness quoteBusiness;

  @Autowired
  private QuandlService quandlService;

  @Autowired
  private AwesomeService awesomeService;

  @Autowired
  private QuotationRepository quotationRepository;

  @Value("${http.quandl.retry.max-attempts}")
  private Integer maxAttemptsQuandl;

  @Value("${http.awesome.retry.max-attempts}")
  private Integer maxAttemptsAwesome;

  @Test
  @DisplayName("should be quote price success when not exists in database")
  @Transactional
  @Rollback
  void shouldBeQuotePriceSuccessWhenNotExistsInDatabase() {

    final var valueQuote = 525.25;
    final var currencyQuoteValue = 5.415;
    final var value = BigDecimal.valueOf(valueQuote * currencyQuoteValue).setScale(3, RoundingMode.HALF_UP);

    this.defaultMocks(valueQuote, currencyQuoteValue);

    final var quotation = this.quoteBusiness.quotePrice("CULTURE", LocalDate.now());

    assertNotNull(quotation);
    assertEquals(value, quotation.getValue());
  }

  @Test
  @DisplayName("should be quote price success when exists in database")
  @Transactional
  @Rollback
  void shouldBeQuotePriceSuccessWhenExistsInDatabase() {

    final var valueQuote = 525.25;
    final var currencyQuoteValue = 5.415;
    final var value = BigDecimal.valueOf(valueQuote * currencyQuoteValue).setScale(3, RoundingMode.HALF_UP);

    final var quotationSaved = this.quotationRepository.save(Quotation.builder()
        .currencyQuotation(BigDecimal.valueOf(currencyQuoteValue))
        .value(value)
        .originalValue(BigDecimal.valueOf(valueQuote))
        .date(LocalDate.now())
        .culture("CULTURE")
        .build());

    final var quotation = this.quoteBusiness.quotePrice("CULTURE", LocalDate.now());

    assertNotNull(quotation);
    assertEquals(quotationSaved, quotation);
  }

  @Test
  @DisplayName("should be quote price success when exists different culture in database")
  @Transactional
  @Rollback
  void shouldBeQuotePriceSuccessWhenExistsQuotationDifferentCultureInDatabase() {

    final var valueQuote = 525.25;
    final var currencyQuoteValue = 5.415;
    final var value = BigDecimal.valueOf(valueQuote * currencyQuoteValue).setScale(3, RoundingMode.HALF_UP);

    this.defaultMocks(valueQuote, currencyQuoteValue);

    final var quotationSaved = this.quotationRepository.save(Quotation.builder()
        .currencyQuotation(BigDecimal.valueOf(currencyQuoteValue))
        .value(value)
        .originalValue(BigDecimal.valueOf(valueQuote))
        .date(LocalDate.now())
        .culture("CULTURE_OTHER")
        .build());

    final var quotation = this.quoteBusiness.quotePrice("CULTURE", LocalDate.now());

    assertNotNull(quotation);
    assertNotEquals(quotationSaved, quotation);
  }

  @Test
  @DisplayName("should be quote price success when exists different date in database")
  @Transactional
  @Rollback
  void shouldBeQuotePriceSuccessWhenExistsQuotationDifferentDateInDatabase() {

    final var valueQuote = 525.25;
    final var currencyQuoteValue = 5.415;
    final var value = BigDecimal.valueOf(valueQuote * currencyQuoteValue).setScale(3, RoundingMode.HALF_UP);

    this.defaultMocks(valueQuote, currencyQuoteValue);

    final var quotationSaved = this.quotationRepository.save(Quotation.builder()
        .currencyQuotation(BigDecimal.valueOf(currencyQuoteValue))
        .value(value)
        .originalValue(BigDecimal.valueOf(valueQuote))
        .date(LocalDate.now().plusDays(1))
        .culture("CULTURE")
        .build());

    final var quotation = this.quoteBusiness.quotePrice("CULTURE", LocalDate.now());

    assertNotNull(quotation);
    assertNotEquals(quotationSaved, quotation);
  }

  @Test
  @DisplayName("should be quote price error when quandlService quote price value")
  @Transactional
  @Rollback
  void shouldBeQuotePriceErrorWhenQuotePrice() {
    this.quandlService.setClient(BaseServiceTest.client(HttpStatus.BAD_REQUEST.value(), "Bad Request", QuandlResponseSeeder.simple(0.00), false));
    this.awesomeService.setClient(BaseServiceTest.client(HttpStatus.OK.value(), "Success", AwesomeResponseSeeder.simple(0.00), true));

    assertThrows(ServiceException.class, () ->
        this.quoteBusiness.quotePrice("CULTURE", LocalDate.now())
    );

    this.verifyAttemptsOfClient(this.quandlService.getClient(), this.maxAttemptsQuandl);
    this.verifyAttemptsOfClient(this.awesomeService.getClient(), 0);
  }

  @Test
  @DisplayName("should be quote price error when awesomeService quote value currency price")
  @Transactional
  @Rollback
  void shouldBeQuotePriceErrorWhenQuoteValueCurrencyPrice() {
    this.quandlService.setClient(BaseServiceTest.client(HttpStatus.OK.value(), "Ok", QuandlResponseSeeder.simple(500.00), false));
    this.awesomeService.setClient(BaseServiceTest.client(HttpStatus.BAD_REQUEST.value(), "Bad Request", AwesomeResponseSeeder.simple(0.00), true));

    assertThrows(ServiceException.class, () ->
        this.quoteBusiness.quotePrice("CULTURE", LocalDate.now())
    );

    this.verifyAttemptsOfClient(this.quandlService.getClient(), 1);
    this.verifyAttemptsOfClient(this.awesomeService.getClient(), this.maxAttemptsAwesome);
  }

  private void defaultMocks(final Double valueQuote, final Double currencyQuoteValue) {
    this.quandlService.setClient(BaseServiceTest.client(HttpStatus.OK.value(), "Success", QuandlResponseSeeder.simple(valueQuote), false));
    this.awesomeService.setClient(BaseServiceTest.client(HttpStatus.OK.value(), "Success", AwesomeResponseSeeder.simple(currencyQuoteValue), true));
  }

  @SuppressWarnings("rawtypes")
  private void verifyAttemptsOfClient(final OkHttpClient client, int times) {
    final var interceptor = (FakeInterceptor) client.interceptors()
        .stream()
        .filter(i -> i.getClass().equals(FakeInterceptor.class))
        .findFirst()
        .orElseThrow();

    assertEquals(times, interceptor.getAttempts().get());
  }
}