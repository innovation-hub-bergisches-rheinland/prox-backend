package de.innovationhub.prox.modules.auth.application.exception;

public class UnauthorizedAccessException extends RuntimeException {
  public UnauthorizedAccessException() {
    super("Unauthorized access");
  }
}
