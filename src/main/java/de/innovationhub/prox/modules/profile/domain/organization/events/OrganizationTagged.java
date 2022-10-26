package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record OrganizationTagged(
    UUID organizationId,
    UUID tagCollection
) implements DomainEvent {
}
