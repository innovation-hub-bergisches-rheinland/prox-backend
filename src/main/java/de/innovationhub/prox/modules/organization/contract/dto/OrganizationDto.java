package de.innovationhub.prox.modules.organization.contract.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(name = "Organization")
public record OrganizationDto(
    UUID id,
    String name,
    OrganizationProfileDto profile,
    List<TagDto> tags,
    String logoUrl,
    @JsonProperty("_permissions")
    OrganizationPermissions permissions,
    Instant createdAt,
    Instant modifiedAt
) {

  public OrganizationDto(
      UUID id
  ) {
    this(id, null, null, null, null, null, null, null);
  }
}
