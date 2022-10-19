package de.innovationhub.prox.modules.profile.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record LecturerProfileUpdated(
    UUID lecturerId,
    String affiliation,
    String subject,
    String vita,
    List<String> publications,
    String room,
    String consultationHour,
    String email,
    String telephone,
    String homepage,
    String collegePage
) implements Event {
  public static LecturerProfileUpdated from(UUID lecturerId, LecturerProfile profile) {
    return new LecturerProfileUpdated(
        lecturerId,
        profile.getAffiliation(),
        profile.getSubject(),
        profile.getVita(),
        new ArrayList<>(profile.getPublications()),
        profile.getRoom(),
        profile.getConsultationHour(),
        profile.getEmail(),
        profile.getTelephone(),
        profile.getHomepage(),
        profile.getCollegePage()
    );
  }
}
