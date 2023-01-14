package de.innovationhub.prox.modules.organization.domain.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.organization.domain.OrganizationProfile;
import java.util.UUID;

public record OrganizationProfileUpdated(
    UUID organizationId,
    OrganizationProfile profile
) implements DomainEvent {
}
