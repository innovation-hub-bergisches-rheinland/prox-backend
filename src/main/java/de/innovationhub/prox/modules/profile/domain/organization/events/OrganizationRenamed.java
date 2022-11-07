package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record OrganizationRenamed(
    UUID organizationId,
    String name
) implements DomainEvent {
}
