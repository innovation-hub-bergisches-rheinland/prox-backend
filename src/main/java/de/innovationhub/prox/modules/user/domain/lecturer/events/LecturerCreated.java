package de.innovationhub.prox.modules.user.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.user.domain.lecturer.Lecturer;
import java.util.UUID;

public record LecturerCreated(
    UUID id,
    UUID userId,
    String name
) implements DomainEvent {
  public static LecturerCreated from(Lecturer lecturer) {
    return new LecturerCreated(
        lecturer.getId(),
        lecturer.getUserId(),
        lecturer.getDisplayName()
    );
  }
}
