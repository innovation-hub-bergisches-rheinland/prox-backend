package de.innovationhub.prox.modules.recommendation.application.dto;

import de.innovationhub.prox.modules.organization.application.dto.OrganizationDto;
import de.innovationhub.prox.modules.project.application.project.dto.ProjectDto;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDto;
import java.util.List;

public record RecommendationResponse(
    List<RecommendationResult<UserProfileDto>> lecturers,
    List<RecommendationResult<OrganizationDto>> organizations,
    List<RecommendationResult<ProjectDto>> projects
) {
  public record RecommendationResult<T>(Double confidenceScore, T item) {
  }
}
