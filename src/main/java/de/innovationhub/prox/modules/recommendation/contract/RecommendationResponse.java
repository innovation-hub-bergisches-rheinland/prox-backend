package de.innovationhub.prox.modules.recommendation.contract;

import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import java.util.List;

public record RecommendationResponse(
    List<RecommendationResult<UserProfileDto>> lecturers,
    List<RecommendationResult<OrganizationDto>> organizations,
    List<RecommendationResult<ProjectDto>> projects
) {

  public record RecommendationResult<T>(Double confidenceScore, T item) {

    public RecommendationResult(Double confidenceScore, T item) {
      if (confidenceScore < 0) {
        throw new IllegalArgumentException("Confidence score must be positive");
      }

      this.confidenceScore = Math.min(confidenceScore, 1);
      this.item = item;
    }
  }
}
