package de.innovationhub.prox.modules.user.domain.profile.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.Set;
import java.util.UUID;

public record UserProfileTagCollectionUpdated(
    UUID userProfileId,
    UUID tagCollectionId
) implements DomainEvent {
}
