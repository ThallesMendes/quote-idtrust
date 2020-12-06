package br.com.idtrust.pricequote.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class BaseServiceTest {

  public static <T extends Serializable> OkHttpClient client(
      final Integer statusCode,
      final String message,
      final T body,
      final boolean isCollection) {

    return new OkHttpClient
        .Builder()
        .addInterceptor(new FakeInterceptor<>(statusCode, message, body, isCollection, new AtomicInteger()))
        .build();
  }

  @Getter
  @AllArgsConstructor
  public static class FakeInterceptor<T extends Serializable> implements Interceptor {

    private final Integer statusCode;
    private final String message;
    private final T body;
    private final boolean isCollection;
    private AtomicInteger attempts;

    @Override
    public Response intercept(Chain chain) throws IOException {

      final var mapper = new ObjectMapper().findAndRegisterModules();
      this.attempts.incrementAndGet();

      return new Response.Builder()
          .code(this.getStatusCode())
          .message(message)
          .request(chain.request())
          .protocol(Protocol.HTTP_1_1)
          .body(ResponseBody.create(MediaType.parse("application/json"),
              mapper.writeValueAsBytes(isCollection ? Collections.singletonList(body) : body)))
          .build();
    }
  }
}