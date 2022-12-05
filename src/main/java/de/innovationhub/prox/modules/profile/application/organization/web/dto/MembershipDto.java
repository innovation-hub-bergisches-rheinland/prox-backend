package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "Membership")

public record MembershipDto(
    UUID member,
    String name,
    OrganizationRole role
) { }
