package de.innovationhub.prox.modules.recommendation.application.dto;

import de.innovationhub.prox.modules.organization.contract.OrganizationView;
import de.innovationhub.prox.modules.project.contract.ProjectView;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileView;
import java.util.List;

public record RecommendationResponse(
    List<RecommendationResult<UserProfileView>> lecturers,
    List<RecommendationResult<OrganizationView>> organizations,
    List<RecommendationResult<ProjectView>> projects
) {
  public record RecommendationResult<T>(Double confidenceScore, T item) {
  }
}
