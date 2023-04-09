package de.innovationhub.prox.modules.project.application.project.dto;

import java.util.UUID;

public record ProjectTagDto(
    UUID id,
    String tagName
) {}
