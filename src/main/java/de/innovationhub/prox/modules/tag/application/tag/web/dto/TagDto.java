package de.innovationhub.prox.modules.tag.application.tag.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "Tag", description = "A tag")
public record TagDto(
    UUID id,
    String tag,
    Instant createdAt,
    Instant modifiedAt
) {

}
