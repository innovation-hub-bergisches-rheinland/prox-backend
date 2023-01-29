package de.innovationhub.prox.modules.user.domain.profile.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.UUID;

public record LecturerProfileCreated(
    UUID userProfileId,
    UUID lecturerProfileId
) implements DomainEvent {
}
