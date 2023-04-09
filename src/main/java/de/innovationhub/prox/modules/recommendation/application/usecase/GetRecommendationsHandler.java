package de.innovationhub.prox.modules.recommendation.application.usecase;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.ProjectView;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class GetRecommendationsHandler {
  private final UserProfileFacade userProfileFacade;
  private final OrganizationFacade organizationFacade;
  private final ProjectFacade projectFacade;

  public RecommendationResponse handle(RecommendationRequest request) {
    var matchingSupervisors = userProfileFacade.findLecturersWithAnyTags(request.seedTags());
    var matchingOrganizations = organizationFacade.findAllWithAnyTags(request.seedTags());
    var matchingProjects = projectFacade.findAllWithAnyTags(request.seedTags());

    var projectSupervisorIds = matchingProjects
        .stream()
        .filter(p -> p.supervisors() != null && p.supervisors().size() > 0)
        .flatMap(p -> p.supervisors().stream())
        .collect(Collectors.toSet());
    var supervisorsOfMatchingProjects = userProfileFacade.findLecturersByIds(projectSupervisorIds);

    var projectPartnerIds = matchingProjects
        .stream()
        .map(ProjectView::partner)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    var organizationsOfMatchingProjects = organizationFacade.findAllByIds(projectPartnerIds);

    // TODO:
    // 1. Get all supervisors which match the seed tags
    // 2. Get all organizations which match the seed tags
    // 3. Get all projects which match the seed tags
    // 3.1. Get supervisors of those projects
    // 3.2. Get partners (organizations) of those projects
    // 4. Based on those results, calculate a confidence score for each supervisor, organization and project
    // 4.1 The jaccard index can be used to calculate the confidence score for (1, 2, 3)
    //     Another option is to simply count the number of matching tags
    // 4.2 The confidence score for (3.1, 3.2) can be calculated based on the number of matching tags
    // 4.3 We can then use both scores to calculate a final confidence score
    // 5. Return the top results for each category together with the confidence score
    return null;
  }
}
