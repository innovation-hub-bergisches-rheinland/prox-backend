package de.innovationhub.prox.modules.project.application.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(name = "SetProjectTagsRequest")
public record SetProjectTagsRequestDto(
    List<UUID> tags
) {

}
