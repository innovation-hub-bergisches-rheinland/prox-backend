package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(name = "SetOrganizationTagsResponse")
public record SetOrganizationTagsResponseDto(
    List<UUID> tags
) {

}
