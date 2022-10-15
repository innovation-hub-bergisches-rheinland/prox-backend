package de.innovationhub.prox.project.project.exception;

public class InvalidProjectStateTransitionException extends RuntimeException {
  public InvalidProjectStateTransitionException(String message) {
    super(message);
  }
}
