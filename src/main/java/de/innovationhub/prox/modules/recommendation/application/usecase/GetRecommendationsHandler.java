package de.innovationhub.prox.modules.recommendation.application.usecase;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto.ReadSupervisorDto;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.recommendation.domain.calc.JaccardIndexCalculator;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class GetRecommendationsHandler {

  private final UserProfileFacade userProfileFacade;
  private final OrganizationFacade organizationFacade;
  private final ProjectFacade projectFacade;
  private final JaccardIndexCalculator jaccardIndexCalculator = new JaccardIndexCalculator();

  @Cacheable(CacheConfig.RECOMMENDATIONS)
  public RecommendationResponse handle(final RecommendationRequest request) {
    // 1. Get all supervisors which match the seed tags
    // 2. Get all organizations which match the seed tags
    // 3. Get all projects which match the seed tags
    final var matchingSupervisors = userProfileFacade.findLecturersWithAnyTags(request.seedTags());
    final var matchingOrganizations = organizationFacade.findAllWithAnyTags(request.seedTags());
    final var matchingProjects = projectFacade.findAllWithAnyTags(request.seedTags());

    // 3.1. Get supervisors of those projects
    final var supervisorIds = matchingProjects
        .stream()
        .filter(p -> p.supervisors() != null && !p.supervisors().isEmpty())
        .flatMap(p -> p.supervisors().stream())
        .map(ReadSupervisorDto::id)
        .collect(Collectors.toSet());
    final var supervisorsOfMatchingProjects = userProfileFacade.findLecturersByIds(supervisorIds);

    // 3.2. Get partners (organizations) of those projects
    final var projectPartnerIds = matchingProjects
        .stream()
        .filter(p -> p.partner() != null)
        .map(p -> p.partner().id())
        .collect(Collectors.toSet());
    final var organizationsOfMatchingProjects = organizationFacade.findAllByIds(projectPartnerIds);

    // 4. Based on those results, calculate a confidence score for each supervisor, organization and project
    // 4.1 The overlap coefficent index can be used to calculate the confidence score for (1, 2, 3)
    //     Another option is to simply count the number of matching tags
    // 4.2 The confidence score for (3.1, 3.2) can be calculated based on the number of matching tags
    // 4.3 We can then use both scores to calculate a final confidence score
    final HashMap<UUID, Double> lecturerConfidenceScores = new HashMap<>();
    final HashMap<UUID, Double> organizationConfidenceScores = new HashMap<>();
    final HashMap<UUID, Double> projectConfidenceScores = new HashMap<>();

    for (var supervisor : matchingSupervisors) {
      final var score = jaccardIndexCalculator.calculate(request.seedTags(),
          supervisor.tags().stream().map(
              TagDto::id).toList());
      lecturerConfidenceScores.put(supervisor.userId(), score);
    }

    for (var supervisor : supervisorsOfMatchingProjects) {
      final Predicate<ProjectDto> isSupervisor = (p) -> p.supervisors() != null && p.supervisors()
          .stream().anyMatch(s -> s.id().equals(supervisor.userId()));
      final var projectsWithSupervisor = matchingProjects.stream()
          .filter(isSupervisor);

      final var score = projectsWithSupervisor
          .reduce(0.0, (s, p) -> s + jaccardIndexCalculator.calculate(
              extractTagIds(p.tags()),
              extractTagIds(supervisor.tags())), Double::sum);
      lecturerConfidenceScores.compute(supervisor.userId(),
          (k, v) -> v == null ? score : v + score);
    }

    for (var organization : matchingOrganizations) {
      final var score = jaccardIndexCalculator.calculate(request.seedTags(),
          organization.tags().stream().map(
              TagDto::id).toList());
      organizationConfidenceScores.put(organization.id(), score);
    }

    for (var organization : organizationsOfMatchingProjects) {

      final var projectsWithOrganization = matchingProjects.stream()
          .filter(p -> p.partner() != null && p.partner().id().equals(organization.id()));

      final var score = projectsWithOrganization
          .reduce(0.0, (s, o) -> s + jaccardIndexCalculator.calculate(
              extractTagIds(o.tags()),
              extractTagIds(organization.tags())), Double::sum);
      organizationConfidenceScores.compute(organization.id(),
          (k, v) -> v == null ? score : v + score);
    }

    for (var project : matchingProjects) {
      final var score = jaccardIndexCalculator.calculate(request.seedTags(), project.tags().stream().map(
          TagDto::id).toList());
      projectConfidenceScores.put(project.id(), score);
    }

    // 5. Return the top results for each category together with the confidence score
    final var topFiveLecturers = streamTopFive(lecturerConfidenceScores)
        .map(e -> new RecommendationResponse.RecommendationResult<>(e.getValue(),
            userProfileFacade.getByUserId(e.getKey()).orElse(null)))
        .toList();

    final var topFiveOrganizations = streamTopFive(organizationConfidenceScores)
        .map(e -> new RecommendationResponse.RecommendationResult<>(e.getValue(),
            organizationFacade.get(e.getKey()).orElse(null)))
        .toList();

    final var topFiveProjects = streamTopFive(projectConfidenceScores)
        .map(e -> new RecommendationResponse.RecommendationResult<>(e.getValue(),
            projectFacade.get(e.getKey()).orElse(null)))
        .toList();

    return new RecommendationResponse(topFiveLecturers, topFiveOrganizations, topFiveProjects);
  }

  private List<UUID> extractTagIds(final List<TagDto> tags) {
    return tags.stream().map(TagDto::id).toList();
  }

  private <T> Stream<Entry<T, Double>> streamTopFive(final Map<T, Double> map) {
    return map.entrySet().stream()
        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
        .filter(e -> e.getValue() > 0.0)
        .limit(5);
  }
}
