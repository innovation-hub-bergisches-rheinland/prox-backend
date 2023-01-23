package de.innovationhub.prox.modules.tag.application.tag.dto;

import java.util.List;

public record SynchronizeTagsResponse(
    List<TagDto> tags
) {

}
