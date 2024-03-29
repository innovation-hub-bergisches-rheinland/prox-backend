package de.innovationhub.prox.modules.user.application.profile.dto;

import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "SetLecturerTagsResponse")
public record SetLecturerTagsResponseDto(
    List<TagDto> tags
) {

}
