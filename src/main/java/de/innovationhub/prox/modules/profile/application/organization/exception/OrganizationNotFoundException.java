package de.innovationhub.prox.modules.profile.application.organization.exception;

public class OrganizationNotFoundException extends RuntimeException {
  public OrganizationNotFoundException() {
    super("Organization not found");
  }
}
