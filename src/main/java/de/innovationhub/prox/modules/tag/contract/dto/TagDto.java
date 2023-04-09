package de.innovationhub.prox.modules.tag.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "Tag", description = "A tag")
public record TagDto(
    UUID id,
    String tagName,
    Instant createdAt,
    Instant modifiedAt
) {

}
