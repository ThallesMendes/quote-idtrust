package br.com.idtrust.pricequote.common.service;

import static java.text.MessageFormat.format;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import br.com.idtrust.pricequote.common.exception.ServiceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.springframework.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Slf4j
public abstract class BaseService<T> {

  private static final Integer DEFAULT_TIMEOUT = 5000;

  @Setter
  @Getter
  private OkHttpClient client;

  public BaseService(final int timeout) {

    this.client = new OkHttpClient.Builder()
        .connectTimeout(timeout, TimeUnit.MILLISECONDS)
        .readTimeout(timeout, TimeUnit.MILLISECONDS)
        .addInterceptor(chain -> {
          final var request = chain.request()
              .newBuilder()
              .addHeader("Accept", "application/json")
              .build();
          return chain.proceed(request);
        })
        .build();

  }

  public BaseService() {
    this(DEFAULT_TIMEOUT);
  }

  protected abstract String getBaseUrl();
  protected abstract String getServiceName();

  protected T getApi() {

    final var builder = new Retrofit.Builder()
        .baseUrl(this.getBaseUrl())
        .client(client);

    builder.addConverterFactory(JacksonConverterFactory.create());

    var retrofit = builder.build();
    return retrofit.create(this.getTypeOfClient());
  }

  protected <R> Optional<R> executeAndVerifyStatusCode(final Call<R> request, final HttpStatus expectedStatus) {

    Response<R> response = null;

    try {
      response = request.execute();

      if (response.raw().code() != expectedStatus.value()) {
        throw new ServiceException(this.getServiceName(), formatBody(response.errorBody()));
      }

      return Optional.ofNullable(response.body());

    } catch (IOException ex) {
      throw new ServiceException(this.getServiceName(), "{}", ex);
    } finally {
      this.loggerRequest(request, response);
    }
  }

  private void loggerRequest(final Call<?> request, final Response<?> response) {
    final var data = new HashMap<>();
    data.put("request_method", request.request().method());
    data.put("path", request.request().url().uri().getPath());
    data.put("request_header", formatHeaders(request.request().headers()));
    data.put("request_body", formatBody(request.request().body()));

    var statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    if (Objects.nonNull(response) && Objects.nonNull(response.raw())) {
      data.put("url", response.raw().request().url().toString());
      data.put("response_body", response.body() != null ? formatBody(response.body()) : formatBody(response.errorBody()));
      data.put("response_header", formatHeaders(response.raw().headers()));

      statusCode = response.raw().code();

      data.put("status_code", statusCode);
      data.put("latency_seconds", (double) (response.raw().receivedResponseAtMillis() - response.raw().sentRequestAtMillis()) / 1000L);
    }

    var message = format("Call: {0}", this.getServiceName());

    if (statusCode < 300) {
      log.info(Markers.appendEntries(data), message);
    } else if (statusCode < 500) {
      log.warn(Markers.appendEntries(data), message);
    } else {
      log.error(Markers.appendEntries(data), message);
    }
  }

  @SuppressWarnings("unchecked")
  private Class<T> getTypeOfClient() {
    return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  private static String formatBody(final Object body) {

    if (Objects.nonNull(body)) {

      try {
        if (body instanceof ResponseBody responseBody) {
          return responseBody.string();
        } else if (body instanceof RequestBody requestBody) {
          var buffer = new Buffer();
          requestBody.writeTo(buffer);
          return buffer.readUtf8();
        } else {
          return new ObjectMapper().writeValueAsString(body);
        }

      } catch (Exception ex) {
        return "";
      }
    }

    return "";
  }

  private static String formatHeaders(final Headers headers) {
    final var format = new StringBuilder();

    headers.names().forEach(key -> format.append(headers.get(key)).append(","));

    return format.toString();
  }

}
