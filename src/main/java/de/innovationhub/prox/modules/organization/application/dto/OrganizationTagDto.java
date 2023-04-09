package de.innovationhub.prox.modules.organization.application.dto;

import java.util.UUID;

public record OrganizationTagDto(
    UUID id,
    String tagName
) {}

