package de.innovationhub.prox.modules.project.application.project.web.dto;

import java.util.List;
import java.util.UUID;

public record SetProjectTagsRequestDto(
    List<UUID> tags
) {

}
