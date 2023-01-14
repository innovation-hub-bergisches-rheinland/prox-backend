package de.innovationhub.prox.modules.user.application.account.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException() {
    super();
  }
}
