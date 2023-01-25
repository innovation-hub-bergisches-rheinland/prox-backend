package de.innovationhub.prox.modules.user.domain.profile.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import java.util.UUID;

public record UserProfileCreated(
    UUID id,
    UUID userId,
    String displayName,
    String vita,
    ContactInformation contactInformation
) implements DomainEvent {
}
