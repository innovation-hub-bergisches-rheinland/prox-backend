package de.innovationhub.prox.commons.exception;

/**
 * A exception raised for cases where it is impossible to fail.
 */
public class ImpossibleException extends RuntimeException {
  public ImpossibleException(String message) {
    super(message);
  }
}
