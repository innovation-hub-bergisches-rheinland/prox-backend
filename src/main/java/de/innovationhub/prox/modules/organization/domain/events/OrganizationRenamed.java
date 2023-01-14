package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record OrganizationRenamed(
    UUID organizationId,
    String name
) implements DomainEvent {
}
