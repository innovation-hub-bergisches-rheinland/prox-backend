package de.innovationhub.prox.modules.user.domain.lecturer.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileInformation;
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
) implements DomainEvent {
  public static LecturerProfileUpdated from(UUID lecturerId, LecturerProfileInformation profile) {
    return new LecturerProfileUpdated(
        lecturerId,
        profile.getAffiliation(),
        profile.getSubject(),
        profile.getVita(),
        profile.getPublications() != null ? new ArrayList<>(profile.getPublications()) : null,
        profile.getRoom(),
        profile.getConsultationHour(),
        profile.getEmail(),
        profile.getTelephone(),
        profile.getHomepage(),
        profile.getCollegePage()
    );
  }
}
