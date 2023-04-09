package de.innovationhub.prox.modules.project.contract.dto;

import java.util.UUID;

public record ProjectTagDto(
    UUID id,
    String tagName
) {}
