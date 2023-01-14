package de.innovationhub.prox.modules.organization.application.web.dto;

import de.innovationhub.prox.modules.organization.domain.SocialMedia;
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
