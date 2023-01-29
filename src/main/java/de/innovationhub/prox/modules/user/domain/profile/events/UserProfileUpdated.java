package de.innovationhub.prox.modules.user.domain.profile.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import java.util.UUID;

public record UserProfileUpdated(
    UUID id,
    UUID userId,
    String displayName,
    String vita,
    ContactInformation contactInformation
) implements DomainEvent {
}
