package de.innovationhub.prox.modules.profile.infrastructure.persistence;

import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import de.innovationhub.prox.modules.profile.infrastructure.persistence.model.OrganizationEntity;
import de.innovationhub.prox.modules.profile.infrastructure.persistence.model.OrganizationMembershipEntity;
import de.innovationhub.prox.modules.profile.infrastructure.persistence.model.OrganizationMembershipPK;
import java.util.List;
import java.util.UUID;

public class OrganizationEntities {

  public static OrganizationEntity ACME = new OrganizationEntity(UUID.randomUUID(), "ACME Ltd");

  public static List<OrganizationEntity> ORGANIZATIONS = List.of(ACME);

  static {
    ACME.setVita("Lorem Ipsum");
    var memberships =
        new OrganizationMembershipEntity(
            new OrganizationMembershipPK(ACME.getId(), UserEntities.HOMER.getId()),
            UUID.randomUUID(),
            ACME.getId(),
            OrganizationRole.ADMIN);
    ACME.setMemberships(List.of(memberships));
  }
}
