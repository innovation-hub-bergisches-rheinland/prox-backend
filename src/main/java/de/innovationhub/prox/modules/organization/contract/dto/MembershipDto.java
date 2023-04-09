package de.innovationhub.prox.modules.organization.contract.dto;

import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "Membership")

public record MembershipDto(
    UUID member,
    String name,
    OrganizationRole role
) { }
