package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import java.util.UUID;

public record OrganizationTagged(
    UUID organizationId,
    UUID tagCollection
) implements Event {
}
