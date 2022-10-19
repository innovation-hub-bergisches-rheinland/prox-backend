package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import java.util.UUID;

public record OrganizationMemberAdded(
    UUID organizationId,
    Membership membership
) implements Event { }
