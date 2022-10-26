package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.profile.domain.organization.Member;
import java.util.UUID;

public record OrganizationMemberRemoved(
    UUID organizationId,
    Member member
) implements DomainEvent { }
