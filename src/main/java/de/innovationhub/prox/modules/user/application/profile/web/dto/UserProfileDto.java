package de.innovationhub.prox.modules.user.application.profile.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "UserProfileDto")
public record UserProfileDto(
    UUID userId,
    String displayName,
    String avatarUrl,
    LecturerProfileDto lecturerProfile
) {
}
