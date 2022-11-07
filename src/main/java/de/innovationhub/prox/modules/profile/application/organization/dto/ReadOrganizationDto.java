package de.innovationhub.prox.modules.profile.application.organization.dto;

import java.util.List;
import java.util.UUID;

public record ReadOrganizationDto(
    UUID id,
    String name,
    OrganizationProfileDto profile,
    List<String> tags
) { }
