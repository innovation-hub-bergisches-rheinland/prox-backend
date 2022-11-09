package de.innovationhub.prox.modules.project.application.project.web.dto;

import java.util.List;
import java.util.UUID;

public record SetProjectTagsResponseDto(
    List<UUID> tags
) {

}
