package de.innovationhub.prox.modules.recommendation.application.usecase;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.ProjectView;
import de.innovationhub.prox.modules.recommendation.application.calc.JaccardIndexCalculator;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class GetRecommendationsHandler {
  private final UserProfileFacade userProfileFacade;
  private final OrganizationFacade organizationFacade;
  private final ProjectFacade projectFacade;
  private final JaccardIndexCalculator jaccardIndexCalculator = new JaccardIndexCalculator();

  public RecommendationResponse handle(RecommendationRequest request) {
    // 1. Get all supervisors which match the seed tags
    // 2. Get all organizations which match the seed tags
    // 3. Get all projects which match the seed tags
    var matchingSupervisors = userProfileFacade.findLecturersWithAnyTags(request.seedTags());
    var matchingOrganizations = organizationFacade.findAllWithAnyTags(request.seedTags());
    var matchingProjects = projectFacade.findAllWithAnyTags(request.seedTags());

    // 3.1. Get supervisors of those projects
    var projectSupervisorIds = matchingProjects
        .stream()
        .filter(p -> p.supervisors() != null && !p.supervisors().isEmpty())
        .flatMap(p -> p.supervisors().stream())
        .collect(Collectors.toSet());
    var supervisorsOfMatchingProjects = userProfileFacade.findLecturersByIds(projectSupervisorIds);


    // 3.2. Get partners (organizations) of those projects
    var projectPartnerIds = matchingProjects
        .stream()
        .map(ProjectView::partner)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    var organizationsOfMatchingProjects = organizationFacade.findAllByIds(projectPartnerIds);

    // 4. Based on those results, calculate a confidence score for each supervisor, organization and project
    // 4.1 The overlap coefficent index can be used to calculate the confidence score for (1, 2, 3)
    //     Another option is to simply count the number of matching tags
    // 4.2 The confidence score for (3.1, 3.2) can be calculated based on the number of matching tags
    // 4.3 We can then use both scores to calculate a final confidence score
    HashMap<UUID, Double> lecturerConfidenceScores = new HashMap<>();
    HashMap<UUID, Double> organizationConfidenceScores = new HashMap<>();
    HashMap<UUID, Double> projectConfidenceScores = new HashMap<>();

    for (var supervisor : matchingSupervisors) {
      var score = jaccardIndexCalculator.calculate(request.seedTags(), supervisor.tags());
      lecturerConfidenceScores.put(supervisor.id(), score);
    }

    for (var supervisor : supervisorsOfMatchingProjects) {
      var score = jaccardIndexCalculator.calculate(request.seedTags(), supervisor.tags());
      lecturerConfidenceScores.compute(supervisor.id(), (k,v) -> v == null ? score : v + score);
    }

    for (var organization : matchingOrganizations) {
      var score = jaccardIndexCalculator.calculate(request.seedTags(), organization.tags());
      organizationConfidenceScores.put(organization.id(), score);
    }

    for (var organization : organizationsOfMatchingProjects) {
      var score = jaccardIndexCalculator.calculate(request.seedTags(), organization.tags());
      organizationConfidenceScores.compute(organization.id(), (k,v) -> v == null ? score : v + score);
    }

    for (var project : matchingProjects) {
      var score = jaccardIndexCalculator.calculate(request.seedTags(), project.tags());
      projectConfidenceScores.put(project.id(), score);
    }

    // 5. Return the top results for each category together with the confidence score
    var topFiveLecturers = streamTopFive(lecturerConfidenceScores)
        .map(e -> new RecommendationResponse.RecommendationResult<>(e.getValue(), userProfileFacade.getByUserId(e.getKey()).orElse(null)))
        .toList();

    var topFiveOrganizations = streamTopFive(organizationConfidenceScores)
        .map(e -> new RecommendationResponse.RecommendationResult<>(e.getValue(), organizationFacade.get(e.getKey()).orElse(null)))
        .toList();

    var topFiveProjects = streamTopFive(projectConfidenceScores)
        .map(e -> new RecommendationResponse.RecommendationResult<>(e.getValue(), projectFacade.get(e.getKey()).orElse(null)))
        .toList();

    return new RecommendationResponse(topFiveLecturers, topFiveOrganizations, topFiveProjects);
  }

  private <T> Stream<Entry<T, Double>> streamTopFive(Map<T, Double> map) {
    return map.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).limit(5);
  }
}
