package de.innovationhub.prox.modules.organization.application.exception;

public class OrganizationNotFoundException extends RuntimeException {
  public OrganizationNotFoundException() {
    super("Organization not found");
  }
}
