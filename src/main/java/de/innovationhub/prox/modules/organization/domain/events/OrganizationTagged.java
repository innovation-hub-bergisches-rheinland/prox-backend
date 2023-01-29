package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.Collection;
import java.util.UUID;

public record OrganizationTagged(
    UUID organizationId,
    Collection<UUID> tags
) implements DomainEvent {
}
