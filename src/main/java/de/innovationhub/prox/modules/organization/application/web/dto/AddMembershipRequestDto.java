package de.innovationhub.prox.modules.organization.application.web.dto;

import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "AddMembershipRequest")
public record AddMembershipRequestDto(UUID member, OrganizationRole role) {

}
