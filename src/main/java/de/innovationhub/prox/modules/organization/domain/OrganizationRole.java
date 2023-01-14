package de.innovationhub.prox.modules.organization.domain;

public enum OrganizationRole {
  MEMBER(0),
  ADMIN(1);

  private int priority;

  OrganizationRole(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return priority;
  }
}
