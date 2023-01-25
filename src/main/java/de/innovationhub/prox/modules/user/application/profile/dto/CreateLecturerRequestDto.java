package de.innovationhub.prox.modules.user.application.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "CreateLecturerRequest")
public record CreateLecturerRequestDto(
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
      String collegePage
  ) {
  }
}
