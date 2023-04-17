package de.innovationhub.prox.modules.tag.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(name = "TagCollection")
public record TagCollectionDto(
    UUID id,
    List<TagDto> tags
) {

}
