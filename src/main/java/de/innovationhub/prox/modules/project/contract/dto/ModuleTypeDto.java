package de.innovationhub.prox.modules.project.contract.dto;

import java.time.Instant;

public record ModuleTypeDto(
    String key,
    String name,
    Instant createdAt,
    Instant modifiedAt
) {

}
