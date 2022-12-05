package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateMembershipRequest")
public record UpdateMembershipRequestDto(OrganizationRole role) {

}
