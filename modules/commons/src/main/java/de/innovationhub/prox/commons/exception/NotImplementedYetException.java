package de.innovationhub.prox.commons.exception;

public class NotImplementedYetException extends UnsupportedOperationException {
  public NotImplementedYetException() {
    super("Not implemented yet");
  }

  public NotImplementedYetException(String reason) {
    super(reason);
  }
}
