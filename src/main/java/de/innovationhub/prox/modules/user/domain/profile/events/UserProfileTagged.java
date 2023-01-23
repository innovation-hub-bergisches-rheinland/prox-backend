package de.innovationhub.prox.modules.user.domain.profile.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.Set;
import java.util.UUID;

public record UserProfileTagged(
    UUID userProfileId,
    Set<UUID> tags
) implements DomainEvent {
}