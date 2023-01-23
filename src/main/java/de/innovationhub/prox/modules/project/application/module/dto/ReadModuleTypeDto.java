package de.innovationhub.prox.modules.project.application.module.dto;

import java.time.Instant;

public record ReadModuleTypeDto(
    String key,
    String name,
    Instant createdAt,
    Instant modifiedAt
) {

}
