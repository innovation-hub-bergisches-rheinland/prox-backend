package de.innovationhub.prox.modules.organization.application.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(name = "SetOrganizationTagsRequest")

public record SetOrganizationTagsRequestDto(
    List<UUID> tags
) {

}
