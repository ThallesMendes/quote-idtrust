package br.com.idtrust.pricequote.business.impl;

import static java.text.MessageFormat.format;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import javax.transaction.Transactional;

import br.com.idtrust.pricequote.business.QuoteBusiness;
import br.com.idtrust.pricequote.model.Quotation;
import br.com.idtrust.pricequote.repository.QuotationRepository;
import br.com.idtrust.pricequote.service.awesome.AwesomeService;
import br.com.idtrust.pricequote.service.quandl.QuandlService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Autowired))
@Slf4j
class QuoteBusinessImpl implements QuoteBusiness {

  private static final String CURRENCY = "USD";
  private static final String CURRENCY_IN = "BRL";

  private final QuandlService quandlService;
  private final AwesomeService awesomeService;
  private final QuotationRepository quotationRepository;

  @Override
  @Transactional
  public Quotation quotePrice(@NonNull final String culture,
                              @NonNull final LocalDate date) {


    log.info(format("quote culture {0} in {1}",
        culture, date));
    final var quotationExists
        = this.quotationRepository.findFirstByCultureAndDate(culture, date);

    if (quotationExists.isPresent()) {
      log.info(format("quote for culture {0} already exists in database",
          culture));
      return quotationExists.get();
    }

    log.info(format("quote for culture {0} not exists in database, start quotation",
        culture));
    final var originalValue =
        this.quandlService.getQuoteValue(culture, date, date);
    log.info(format("original value for culture {0} is {1}",
        culture, originalValue));

    final var currencyQuoteValue =
        this.awesomeService.getValueCurrencyQuote(CURRENCY, CURRENCY_IN, date, date);
    log.info(format("currency quote value is {0}",
        currencyQuoteValue));

    final var value = originalValue * currencyQuoteValue;
    log.info(format("converted value is {0}",
        value));

    final var quotation = Quotation.builder()
        .culture(culture)
        .value(BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP))
        .originalValue(BigDecimal.valueOf(originalValue).setScale(3, RoundingMode.HALF_UP))
        .currencyQuotation(BigDecimal.valueOf(currencyQuoteValue))
        .date(date)
        .build();

    log.info(format("quote for culture {0} save in database",
        culture));
    return this.quotationRepository.save(quotation);
  }

}
