package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.List;
import java.util.UUID;

public record UpdateLecturerProfile(
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
) implements UseCase {}
