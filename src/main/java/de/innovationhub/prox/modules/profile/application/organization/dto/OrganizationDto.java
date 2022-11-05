package de.innovationhub.prox.modules.profile.application.organization.dto;

import java.util.List;
import java.util.UUID;

public record OrganizationDto(
    UUID id,
    String name,
    OrganizationProfileDto profile,
    List<String> tags
) { }
