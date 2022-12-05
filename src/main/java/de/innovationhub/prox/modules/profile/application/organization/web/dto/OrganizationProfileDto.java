package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.modules.profile.domain.organization.SocialMedia;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(name = "OrganizationProfile")
public record OrganizationProfileDto(
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
