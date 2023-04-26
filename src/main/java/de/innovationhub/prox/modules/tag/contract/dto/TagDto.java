package de.innovationhub.prox.modules.tag.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Schema(name = "Tag", description = "A tag")
public record TagDto(
    UUID id,
    String tagName,
    Set<String> aliases,
    long count,
    Instant createdAt,
    Instant modifiedAt
) {

}
