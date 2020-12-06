package br.com.idtrust.pricequote.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.idtrust.pricequote.common.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class HandlerAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AuthenticationException ex) throws IOException, ServletException {

    final var res = ResponseError.builder()
        .message("Unauthorized - " + ex.getMessage())
        .build();

    response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().println(new ObjectMapper().writeValueAsString(res));
  }
}
