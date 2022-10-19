package de.innovationhub.prox.modules.commons.core;

/**
 * A exception raised for cases where it is impossible to fail.
 */
public class ImpossibleException extends RuntimeException {
  public ImpossibleException(String message) {
    super(message);
  }
}
