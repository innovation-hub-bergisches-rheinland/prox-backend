package de.innovationhub.prox.modules.project.application.discipline.web.dto;

import java.time.Instant;

public record ReadDisciplineDto(
    String key,
    String name,
    Instant createdAt,
    Instant modifiedAt
) {

}
