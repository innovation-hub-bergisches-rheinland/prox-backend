package de.innovationhub.prox.modules.profile.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record LecturerTagged(
    UUID lecturerId,
    UUID tagCollection
) implements DomainEvent {
}
