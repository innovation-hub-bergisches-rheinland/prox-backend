package de.innovationhub.prox.modules.user.application.account.exception;

public class UnauthorizedAccessException extends RuntimeException {
  public UnauthorizedAccessException() {
    super("Unauthorized access");
  }
}
