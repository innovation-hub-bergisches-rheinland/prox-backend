package de.innovationhub.prox.modules.tag.application.tag.web.dto;

import java.time.Instant;
import java.util.UUID;

public record TagDto(
    UUID id,
    String tag,
    Instant createdAt,
    Instant modifiedAt
) {

}
