package de.innovationhub.prox.modules.user.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import java.util.UUID;

public record LecturerCreated(
    UUID id,
    UUID userId,
    String name
) implements DomainEvent {

  public static LecturerCreated from(LecturerProfile lecturerProfile) {
    return new LecturerCreated(
        lecturerProfile.getId(),
        lecturerProfile.getUserId(),
        lecturerProfile.getDisplayName()
    );
  }
}
