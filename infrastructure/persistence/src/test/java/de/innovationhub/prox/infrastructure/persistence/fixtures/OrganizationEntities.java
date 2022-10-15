package de.innovationhub.prox.infrastructure.persistence.fixtures;

import de.innovationhub.prox.infrastructure.persistence.model.OrganizationEntity;
import de.innovationhub.prox.infrastructure.persistence.model.OrganizationMembershipEntity;
import de.innovationhub.prox.infrastructure.persistence.model.OrganizationMembershipPK;
import de.innovationhub.prox.profile.organization.OrganizationRole;
import java.util.List;
import java.util.UUID;

public class OrganizationEntities {

  public static OrganizationEntity ACME = new OrganizationEntity(UUID.randomUUID(), "ACME Ltd");

  public static List<OrganizationEntity> ORGANIZATIONS = List.of(ACME);

  static {
    ACME.setVita("Lorem Ipsum");
    var memberships = new OrganizationMembershipEntity(
      new OrganizationMembershipPK(ACME.getId(), UserEntities.HOMER.getId()),
      UUID.randomUUID(),
      ACME.getId(),
      OrganizationRole.ADMIN
    );
    ACME.setMemberships(List.of(memberships));
  }
}
