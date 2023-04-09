package de.innovationhub.prox.modules.user.application.profile.dto;

import java.util.UUID;

public record UserProfileTagDto(
    UUID id,
    String tagName
) {}
