package de.innovationhub.prox.modules.profile.application.lecturer.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "CreateLecturerRequest")
public record CreateLecturerRequestDto(
    String name,
    CreateLecturerProfileDto profile,
    boolean visibleInPublicSearch
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
