package de.innovationhub.prox.modules.user.application.exception;

public class UnauthorizedAccessException extends RuntimeException {
  public UnauthorizedAccessException() {
    super("Unauthorized access");
  }
}
