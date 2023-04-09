package de.innovationhub.prox.modules.organization.contract.dto;

import java.util.UUID;

public record OrganizationTagDto(
    UUID id,
    String tagName
) {}

