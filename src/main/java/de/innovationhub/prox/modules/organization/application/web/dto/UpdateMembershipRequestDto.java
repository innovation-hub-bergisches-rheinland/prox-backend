package de.innovationhub.prox.modules.organization.application.web.dto;

import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateMembershipRequest")
public record UpdateMembershipRequestDto(OrganizationRole role) {

}
