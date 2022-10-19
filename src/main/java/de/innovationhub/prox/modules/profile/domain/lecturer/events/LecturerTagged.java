package de.innovationhub.prox.modules.profile.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import java.util.List;
import java.util.UUID;

public record LecturerTagged(
    UUID lecturerId,
    List<UUID> tags
) implements Event {
  public static LecturerTagged from(UUID lecturerId, List<UUID> tags) {
    return new LecturerTagged(lecturerId, tags);
  }
}
