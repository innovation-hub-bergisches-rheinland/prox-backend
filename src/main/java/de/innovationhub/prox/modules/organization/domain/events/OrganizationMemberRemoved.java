package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.UUID;

public record OrganizationMemberRemoved(
    UUID organizationId,
    UUID member
) implements DomainEvent { }
