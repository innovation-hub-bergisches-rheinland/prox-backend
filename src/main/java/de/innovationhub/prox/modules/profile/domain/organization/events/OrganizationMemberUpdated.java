package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import java.util.UUID;

public record OrganizationMemberUpdated(
    UUID organizationId,
    Membership updatedMembership
) implements DomainEvent { }
