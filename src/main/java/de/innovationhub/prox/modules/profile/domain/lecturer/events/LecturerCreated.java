package de.innovationhub.prox.modules.profile.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import java.util.UUID;

public record LecturerCreated(
    UUID id,
    UUID userId,
    String name
) implements Event {
  public static LecturerCreated from(Lecturer lecturer) {
    return new LecturerCreated(
        lecturer.getId(),
        lecturer.getUserId(),
        lecturer.getName()
    );
  }
}