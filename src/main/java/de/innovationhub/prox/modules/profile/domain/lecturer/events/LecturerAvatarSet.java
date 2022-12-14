package de.innovationhub.prox.modules.profile.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record LecturerAvatarSet(
    UUID lecturerId,
    String avatarKey
) implements DomainEvent {
}
