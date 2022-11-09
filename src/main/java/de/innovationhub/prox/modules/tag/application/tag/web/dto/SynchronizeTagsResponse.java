package de.innovationhub.prox.modules.tag.application.tag.web.dto;

import java.util.List;
import java.util.UUID;

public record SynchronizeTagsResponse(
    List<TagDto> tags
) {

}
