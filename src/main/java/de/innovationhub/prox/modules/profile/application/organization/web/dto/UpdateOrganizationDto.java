package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.modules.profile.domain.organization.SocialMedia;
import java.util.Map;

public record UpdateOrganizationDto(
    String name,
    UpdateOrganizationProfileDto profile
) {

  public record UpdateOrganizationProfileDto(
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
