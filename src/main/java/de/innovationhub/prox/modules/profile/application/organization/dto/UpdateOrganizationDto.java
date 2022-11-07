package de.innovationhub.prox.modules.profile.application.organization.dto;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import de.innovationhub.prox.modules.profile.domain.organization.SocialMedia;
import java.util.List;
import java.util.Map;

public record UpdateOrganizationDto(
    String name,
    UpdateOrganizationProfileDto profile
) implements UseCase {
  public record UpdateOrganizationProfileDto(
      String foundingDate,
      String numberOfEmployees,
      String homepage,
      String contactEmail,
      String vita,
      String headquarter,
      List<String> quarters,
      Map<SocialMedia, String> socialMediaHandles
  ) { }
}
