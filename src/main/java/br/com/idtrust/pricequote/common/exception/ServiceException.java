package br.com.idtrust.pricequote.common.exception;

import static java.text.MessageFormat.format;

public class ServiceException extends RuntimeException {
  private static final long serialVersionUID = -9094847967693874662L;

  private final String serviceName;
  private final String errorBody;

  public ServiceException(final String serviceName,
                          final String errorBody,
                          final Throwable ex) {
    super(format("Error in call {0}", serviceName), ex);
    this.serviceName = serviceName;
    this.errorBody = errorBody;
  }

  public ServiceException(final String serviceName,
                          final String errorBody) {
    this(serviceName, errorBody, null);
  }
}
