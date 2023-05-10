package de.innovationhub.prox.modules.project.contract.dto;

import java.time.Instant;

public record DisciplineDto(
    String key,
    String name,
    Instant createdAt,
    Instant modifiedAt
) {

}
