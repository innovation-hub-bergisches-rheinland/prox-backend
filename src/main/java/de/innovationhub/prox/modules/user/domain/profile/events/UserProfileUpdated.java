package de.innovationhub.prox.modules.user.domain.profile.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record UserProfileUpdated(
    UUID id,
    UUID userId,
    String displayName,
    String vita
) implements DomainEvent {
}
