package de.innovationhub.prox.modules.user.application.profile.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateUserProfileRequest")
public record CreateUserProfileRequestDto(
    String displayName
) {
}
