package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import java.util.UUID;

public record ReadOrganizationMembershipDto(
    UUID id,
    OrganizationRole role
) { }
