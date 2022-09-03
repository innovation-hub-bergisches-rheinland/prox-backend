package de.innovationhub.prox.profile.organization;

import lombok.Data;

@Data
public class Membership {

  private OrganizationRole role;

  public Membership(OrganizationRole role) {
    this.role = role;
  }
}
