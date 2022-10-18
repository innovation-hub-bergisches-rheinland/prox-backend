package de.innovationhub.prox.modules.profile.domain.organization;

import lombok.Data;

@Data
public class Membership {

  private OrganizationRole role;

  public Membership(OrganizationRole role) {
    this.role = role;
  }
}
