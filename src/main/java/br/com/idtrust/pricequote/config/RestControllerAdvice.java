package br.com.idtrust.pricequote.config;

import static java.text.MessageFormat.format;

import br.com.idtrust.pricequote.common.exception.EntityNotFoundException;
import br.com.idtrust.pricequote.common.exception.ServiceException;
import br.com.idtrust.pricequote.common.response.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice {

  @ExceptionHandler({ServiceException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseError serviceException(final ServiceException ex) {

    log.error(ex.getMessage(), ex);

    return ResponseError.builder()
        .message(ex.getMessage())
        .build();
  }

  @ExceptionHandler({EntityNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ResponseError entityNotFound(final EntityNotFoundException ex) {

    log.error(ex.getMessage(), ex);

    return ResponseError.builder()
        .message(ex.getMessage())
        .build();
  }

  @ExceptionHandler({Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseError exceptionGeneric(final Exception ex) {

    final var message = format("Unknown error ! Class: {0} Message: {1}",
        ex.getClass().getSimpleName(),
        ex.getMessage());

    log.error(message, ex);

    return ResponseError.builder()
        .message(message)
        .build();
  }

}
