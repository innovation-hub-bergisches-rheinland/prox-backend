package de.innovationhub.prox.modules.user.application.lecturer.exception;

public class LecturerNotFoundException extends RuntimeException {
  public LecturerNotFoundException() {
    super("Lecturer not found");
  }
}
