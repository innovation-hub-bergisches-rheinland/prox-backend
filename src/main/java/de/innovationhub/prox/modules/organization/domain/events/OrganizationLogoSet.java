package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.UUID;

public record OrganizationLogoSet(
    UUID lecturerId,
    String logoKey
) implements DomainEvent {
}
