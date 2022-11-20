package de.innovationhub.prox.modules.project.application.project.exception;

public class ProjectNotFoundException extends RuntimeException {
  public ProjectNotFoundException() {
    super("Project not found");
  }
}
