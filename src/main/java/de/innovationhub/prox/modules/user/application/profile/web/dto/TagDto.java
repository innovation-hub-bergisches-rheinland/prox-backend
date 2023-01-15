package de.innovationhub.prox.modules.user.application.profile.web.dto;

import java.util.UUID;

public record TagDto(
    UUID id,
    String tagName
) {}
