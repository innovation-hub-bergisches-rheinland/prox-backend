package de.innovationhub.prox.modules.profile.domain.organization.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationProfile;
import java.util.UUID;

public record OrganizationProfileUpdated(
    UUID organizationId,
    OrganizationProfile profile
) implements DomainEvent {
}
