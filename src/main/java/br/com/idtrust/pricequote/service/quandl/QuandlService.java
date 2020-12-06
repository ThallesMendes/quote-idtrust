package br.com.idtrust.pricequote.service.quandl;

import java.time.LocalDate;
import java.util.Optional;

import br.com.idtrust.pricequote.common.exception.EntityNotFoundException;
import br.com.idtrust.pricequote.common.exception.ServiceException;
import br.com.idtrust.pricequote.common.service.BaseService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class QuandlService extends BaseService<Quandl> {

  private static final String SERVICE_NAME = "Quandl";

  private final String host;
  private final String key;

  @Autowired
  QuandlService(@Value("${http.quandl.host}") final String host,
                @Value("${http.quandl.key}") final String key) {
    this.host = host;
    this.key = key;
  }

  @Retryable(value = ServiceException.class,
      maxAttemptsExpression = "${http.quandl.retry.max-attempts}",
      backoff = @Backoff(delayExpression = "${http.quandl.retry.backoff-delay}"))
  public Optional<QuandlResponse> getQuote(@NonNull final String culture,
                                           @NonNull final LocalDate startDate,
                                           @NonNull final LocalDate endDate) {

    final var request = this.getApi().getQuote(culture, startDate, endDate, this.key);
    return this.executeAndVerifyStatusCode(request, HttpStatus.OK);
  }

  @Retryable(value = ServiceException.class,
      maxAttemptsExpression = "${http.quandl.retry.max-attempts}",
      backoff = @Backoff(delayExpression = "${http.quandl.retry.backoff-delay}"))
  public Double getQuoteValue(final String culture,
                              final LocalDate startDate,
                              final LocalDate endDate) {
    return this.getQuote(culture, startDate, endDate)
        .map(QuandlResponse::getDataset)
        .map(QuandlResponse.Dataset::getData)
        .filter(d -> !d.isEmpty())
        .flatMap(d -> d.stream().findFirst())
        .filter(d -> d.size() > 1)
        .map(q -> q.get(1))
        .filter(q -> q instanceof Double)
        .map(q -> (Double)q)
        .orElseThrow(() -> new EntityNotFoundException("Quote Value"));
  }

  @Override
  protected String getBaseUrl() {
    return this.host;
  }

  @Override
  public String getServiceName() {
    return SERVICE_NAME;
  }
}
