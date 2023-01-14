package de.innovationhub.prox.modules.organization.application.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(name = "Organization")
public record OrganizationDto(
    UUID id,
    String name,
    OrganizationProfileDto profile,
    List<String> tags,
    String logoUrl,
    @JsonProperty("_permissions")
    OrganizationPermissions permissions,
    Instant createdAt,
    Instant modifiedAt
) { }