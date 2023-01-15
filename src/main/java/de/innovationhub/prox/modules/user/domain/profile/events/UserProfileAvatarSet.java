package de.innovationhub.prox.modules.user.domain.profile.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record UserProfileAvatarSet(
    UUID profileId,
    String avatarKey
) implements DomainEvent {
}
