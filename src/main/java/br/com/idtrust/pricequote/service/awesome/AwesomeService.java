package br.com.idtrust.pricequote.service.awesome;

import java.time.LocalDate;
import java.util.Optional;

import br.com.idtrust.pricequote.common.exception.EntityNotFoundException;
import br.com.idtrust.pricequote.common.exception.ServiceException;
import br.com.idtrust.pricequote.common.service.BaseService;
import br.com.idtrust.pricequote.common.util.DateUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class AwesomeService extends BaseService<Awesome> {

  private static final String SERVICE_NAME = "Awesome";
  private static final String FORMAT_DATE_QUERY = "yyyyMMdd";

  private final String host;

  @Autowired
  AwesomeService(@Value("${http.awesome.host}") final String host) {
    this.host = host;
  }

  @Retryable(value = ServiceException.class,
      maxAttemptsExpression = "${http.awesome.retry.max-attempts}",
      backoff = @Backoff(delayExpression = "${http.awesome.retry.backoff-delay}"))
  public Optional<AwesomeResponse> getCurrencyQuote(@NonNull final String currencyCode,
                                                    @NonNull final String currencyCodeIn,
                                                    @NonNull final LocalDate startDate,
                                                    @NonNull final LocalDate endDate) {

    final var formatStartDate = DateUtil.formatDate(startDate, FORMAT_DATE_QUERY);
    final var formatEndDate = DateUtil.formatDate(endDate, FORMAT_DATE_QUERY);

    final var request = this.getApi()
        .getCurrencyQuote(currencyCode, currencyCodeIn, formatStartDate, formatEndDate);
    final var response = this.executeAndVerifyStatusCode(request, HttpStatus.OK);

    return response
        .filter(q -> !q.isEmpty())
        .flatMap(q -> q.stream().findFirst());
  }

  @Retryable(value = ServiceException.class,
      maxAttemptsExpression = "${http.awesome.retry.max-attempts}",
      backoff = @Backoff(delayExpression = "${http.awesome.retry.backoff-delay}"))
  public Double getValueCurrencyQuote(final String currencyCode,
                                      final String currencyCodeIn,
                                      final LocalDate startDate,
                                      final LocalDate endDate) {
    return this.getCurrencyQuote(currencyCode, currencyCodeIn, startDate, endDate)
        .map(AwesomeResponse::getAsk)
        .orElseThrow(() -> new EntityNotFoundException("Value Currency"));
  }

  @Override
  protected String getBaseUrl() {
    return this.host;
  }

  @Override
  protected String getServiceName() {
    return SERVICE_NAME;
  }
}
