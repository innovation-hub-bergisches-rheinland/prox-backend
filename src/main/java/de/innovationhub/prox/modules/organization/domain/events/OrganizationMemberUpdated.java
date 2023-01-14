package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.organization.domain.Membership;
import java.util.UUID;

public record OrganizationMemberUpdated(
    UUID organizationId,
    Membership updatedMembership
) implements DomainEvent { }
