package br.com.idtrust.pricequote.common.controller;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import br.com.idtrust.pricequote.common.response.Response;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

  @Autowired
  private HttpServletRequest request;

  @Value("${application.version}")
  private String appVersion;

  @SneakyThrows
  protected <T extends Serializable> ResponseEntity<Response> buildResponse(
      final HttpStatus status, final List<T> entity) {

    if (HttpStatus.NO_CONTENT.equals(status)) {
      return ResponseEntity.noContent().build();
    }

    Objects.requireNonNull(entity, "entity cannot be null");

    final var response = Response.builder()
        .data(entity)
        .server(InetAddress.getLocalHost().getHostName())
        .version(this.getAppVersion())
        .build();

    return new ResponseEntity<>(response, status);
  }

  protected <T extends Serializable> ResponseEntity<Response> buildResponse(
      final HttpStatus status, final T entity) {

    return this.buildResponse(status, Collections.singletonList(entity));
  }

  private String getAppVersion() {
    return Optional.ofNullable(this.appVersion)
        .map(t -> t.replace("-SNAPSHOT", ""))
        .orElse(this.appVersion);
  }

}
