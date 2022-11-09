package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import java.util.List;
import java.util.UUID;

public record SetOrganizationTagsRequestDto(
    List<UUID> tags
) {

}
