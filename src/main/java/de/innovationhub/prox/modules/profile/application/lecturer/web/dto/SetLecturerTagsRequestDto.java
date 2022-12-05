package de.innovationhub.prox.modules.profile.application.lecturer.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(name = "SetLecturerTagsRequest")
public record SetLecturerTagsRequestDto(
    List<UUID> tags
) {

}
