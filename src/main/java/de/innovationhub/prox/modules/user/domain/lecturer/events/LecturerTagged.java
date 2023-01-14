package de.innovationhub.prox.modules.user.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.Collection;
import java.util.UUID;

public record LecturerTagged(
    UUID lecturerId,
    Collection<UUID> tags
) implements DomainEvent {
}
