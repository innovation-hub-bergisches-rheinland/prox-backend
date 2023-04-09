package de.innovationhub.prox.modules.tag.application.tag.dto;

import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.List;

public record SynchronizeTagsResponse(
    List<TagDto> tags
) {

}
