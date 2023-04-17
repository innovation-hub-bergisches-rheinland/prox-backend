package de.innovationhub.prox.modules.organization.application.dto;

import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(name = "SetOrganizationTagsResponse")
public record SetOrganizationTagsResponseDto(
    List<TagDto> tags
) {

}
