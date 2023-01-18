package de.innovationhub.prox.modules.user.application.profile.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(name = "SetLecturerTagsRequest")
public record SetTagsRequestDto(
    List<UUID> tags
) {

}
