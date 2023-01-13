package de.innovationhub.prox.modules.commons.core;

public class NotImplementedYetException extends UnsupportedOperationException {
  public NotImplementedYetException() {
    super("Not implemented yet");
  }

  public NotImplementedYetException(String reason) {
    super(reason);
  }
}
