package de.innovationhub.prox.modules.organization.application.dto;

import de.innovationhub.prox.modules.organization.domain.SocialMedia;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(name = "CreateOrganizationRequest")
public record CreateOrganizationRequestDto(
    String name,
    CreateOrganizationProfileDto profile
) {
  @Schema(name = "CreateOrganizationProfileRequest")

  public record CreateOrganizationProfileDto(
      String foundingDate,
      String numberOfEmployees,
      String homepage,
      String contactEmail,
      String vita,
      String headquarter,
      String quarters,
      Map<SocialMedia, String> socialMediaHandles
  ) {

  }
}
