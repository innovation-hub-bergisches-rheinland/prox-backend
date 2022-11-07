package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.modules.profile.domain.organization.SocialMedia;
import java.util.List;
import java.util.Map;

public record CreateOrganizationDto(
    String name,
    CreateOrganizationProfileDto profile
) {

  public record CreateOrganizationProfileDto(
      String foundingDate,
      String numberOfEmployees,
      String homepage,
      String contactEmail,
      String vita,
      String headquarter,
      List<String> quarters,
      Map<SocialMedia, String> socialMediaHandles
  ) {

  }
}
