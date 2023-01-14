package de.innovationhub.prox.modules.user.application.user.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException() {
    super();
  }
}
