package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import java.util.List;
import java.util.UUID;

public record OrganizationCreated(
    UUID id,
    String name,
    List<Membership> members
) implements Event {
  public static OrganizationCreated from(Organization org) {
    return new OrganizationCreated(org.getId(), org.getName(), List.copyOf(org.getMembers()));
  }
}
