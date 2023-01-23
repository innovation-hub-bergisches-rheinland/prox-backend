package de.innovationhub.prox.modules.user.application.profile.dto;

import java.util.UUID;

public record TagDto(
    UUID id,
    String tagName
) {}
