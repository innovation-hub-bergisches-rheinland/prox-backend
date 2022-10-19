package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record OrganizationMemberRemoved(
    UUID organizationId,
    UUID memberId
) implements Event { }
