package de.innovationhub.prox.modules.profile.application.lecturer.exception;

public class LecturerNotFoundException extends RuntimeException {
  public LecturerNotFoundException() {
    super("Lecturer not found");
  }
}
