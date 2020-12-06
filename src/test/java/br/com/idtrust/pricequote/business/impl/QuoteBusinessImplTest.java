package br.com.idtrust.pricequote.business.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import br.com.idtrust.pricequote.business.QuoteBusiness;
import br.com.idtrust.pricequote.model.Quotation;
import br.com.idtrust.pricequote.repository.QuotationRepository;
import br.com.idtrust.pricequote.service.awesome.AwesomeService;
import br.com.idtrust.pricequote.service.quandl.QuandlService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuoteBusinessImplTest {

  @Mock
  private QuandlService quandlService;

  @Mock
  private AwesomeService awesomeService;

  @Mock
  private QuotationRepository quotationRepository;

  private QuoteBusiness quoteBusiness;

  @BeforeEach
  void setUp() {

    Mockito.clearInvocations(this.awesomeService, this.quandlService, this.quotationRepository);

    this.quoteBusiness = new QuoteBusinessImpl(
        this.quandlService,
        this.awesomeService,
        this.quotationRepository);
  }

  @Test
  @DisplayName("should be quote price success when not exists in database")
  void shouldBeQuotePriceSuccessWhenNotExistsInDatabase() {
    this.defaultMocks(200.50, 5.415);

    final var quotation = this.quoteBusiness.quotePrice("CULTURE", LocalDate.now());

    this.verifyInvokes(Invokes.builder()
        .getQuoteValue(1)
        .getValueCurrencyQuote(1)
        .saveQuotation(1)
        .build());

    assertNotNull(quotation);
  }

  @Test
  @DisplayName("should be quote price success when exists in database")
  void shouldBeQuotePriceSuccessWhenExistsInDatabase() {
    when(this.quotationRepository.findFirstByCultureAndDate(anyString(), any(LocalDate.class)))
        .thenReturn(Optional.of(mock(Quotation.class)));

    final var quotation = this.quoteBusiness.quotePrice("CULTURE", LocalDate.now());

    this.verifyInvokes(Invokes.builder()
        .getQuoteValue(0)
        .getValueCurrencyQuote(0)
        .saveQuotation(0)
        .build());

    assertNotNull(quotation);
  }

  private void defaultMocks(final Double originalValue, final Double currencyQuote) {

    when(this.quotationRepository.findFirstByCultureAndDate(anyString(), any(LocalDate.class)))
        .thenReturn(Optional.empty());
    when(this.quotationRepository.save(any(Quotation.class)))
        .thenReturn(mock(Quotation.class));

    when(this.quandlService.getQuoteValue(anyString(), any(LocalDate.class), any(LocalDate.class)))
        .thenReturn(originalValue);

    when(this.awesomeService
        .getValueCurrencyQuote(anyString(), anyString(), any(LocalDate.class), any(LocalDate.class)))
        .thenReturn(currencyQuote);
  }

  private void verifyInvokes(final Invokes invokes) {
    verify(this.quandlService, times(invokes.getQuoteValue))
        .getQuoteValue(anyString(), any(LocalDate.class), any(LocalDate.class));
    verify(this.awesomeService, times(invokes.getValueCurrencyQuote))
        .getValueCurrencyQuote(anyString(), anyString(), any(LocalDate.class), any(LocalDate.class));
    verify(this.quotationRepository, times(invokes.saveQuotation))
        .save(any(Quotation.class));
  }

  @AllArgsConstructor
  @Builder
  @Data
  private static class Invokes {
    private int getQuoteValue;
    private int getValueCurrencyQuote;
    private int saveQuotation;
  }
}