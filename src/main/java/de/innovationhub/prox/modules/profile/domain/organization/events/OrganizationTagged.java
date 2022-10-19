package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record OrganizationTagged(
    UUID organizationId,
    List<UUID> tags
) implements Event {
  public static OrganizationTagged from(UUID organizationId, Collection<UUID> tags) {
    return new OrganizationTagged(organizationId, List.copyOf(tags));
  }
}
