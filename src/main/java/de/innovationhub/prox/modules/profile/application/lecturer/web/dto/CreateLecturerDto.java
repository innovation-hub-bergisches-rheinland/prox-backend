package de.innovationhub.prox.modules.profile.application.lecturer.web.dto;

import java.util.List;

public record CreateLecturerDto(
    String name,
    CreateLecturerProfileDto profile
) {
  public record CreateLecturerProfileDto(
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
  ) {
  }
}
