package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.user.User;
import java.util.UUID;

public record OrganizationMemberUpdated(
    UUID organizationId,
    User member,
    Membership updatedMembership
) implements Event { }
