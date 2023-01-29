package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.organization.domain.Membership;
import de.innovationhub.prox.modules.organization.domain.Organization;
import java.util.List;
import java.util.UUID;

public record OrganizationCreated(
    UUID id,
    String name,
    List<Membership> members
) implements DomainEvent {
  public static OrganizationCreated from(Organization org) {
    return new OrganizationCreated(org.getId(), org.getName(), List.copyOf(org.getMembers()));
  }
}
