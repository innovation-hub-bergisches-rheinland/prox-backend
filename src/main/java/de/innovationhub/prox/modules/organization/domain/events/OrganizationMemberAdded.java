package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.organization.domain.Membership;
import java.util.UUID;

public record OrganizationMemberAdded(
    UUID organizationId,
    Membership membership
) implements DomainEvent { }
