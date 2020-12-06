package br.com.idtrust.pricequote.common.exception;

import static java.text.MessageFormat.format;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(final String entity) {
    super(format("Entity {0} not found", entity));
  }

}
