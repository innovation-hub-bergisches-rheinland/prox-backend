package de.innovationhub.prox.modules.user.application.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(name = "UserProfileDto")
public record UserProfileDto(
    UUID userId,
    String displayName,
    String avatarUrl,

    String vita,
    boolean visibleInPublicSearch,
    LecturerProfileDto lecturerProfile,
    ContactInformationDto contact,
    List<TagDto> tags
) {
  public record ContactInformationDto(
      String email,
      String telephone,
      String homepage
  ) {
  }
}
