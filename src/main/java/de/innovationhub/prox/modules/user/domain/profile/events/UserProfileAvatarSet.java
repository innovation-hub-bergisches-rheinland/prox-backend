package de.innovationhub.prox.modules.user.domain.profile.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.UUID;

public record UserProfileAvatarSet(
    UUID profileId,
    String avatarKey
) implements DomainEvent {
}
