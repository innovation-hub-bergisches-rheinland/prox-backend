package de.innovationhub.prox.modules.project.domain.project.exception;

public class InvalidProjectStateTransitionException extends RuntimeException {

  public InvalidProjectStateTransitionException(String message) {
    super(message);
  }
}
