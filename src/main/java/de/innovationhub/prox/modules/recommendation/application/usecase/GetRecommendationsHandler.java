package de.innovationhub.prox.modules.recommendation.application.usecase;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse.RecommendationResult;
import de.innovationhub.prox.modules.recommendation.domain.calc.OverlapCoefficientCalculator;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import de.innovationhub.prox.modules.user.contract.profile.dto.LecturerProfileDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class GetRecommendationsHandler {

  private static final double LECTURER_PROFILE_MATCH_BOOST_FACTOR = 2.5;
  private static final double ORGANISATION_PROFILE_MATCH_BOOST_FACTOR = 2.5;
  private static final double PROJECT_MATCH_BOOST_FACTOR = 1;
  private static final ProjectState[] PROJECT_STATE_FILTER = {
      ProjectState.OFFERED, ProjectState.PROPOSED, ProjectState.RUNNING
  };

  private final TagCollectionFacade tagCollectionFacade;
  private final UserProfileFacade userProfileFacade;
  private final OrganizationFacade organizationFacade;
  private final ProjectFacade projectFacade;
  private final OverlapCoefficientCalculator overlapCoefficientCalculator = new OverlapCoefficientCalculator();

  @Cacheable(CacheConfig.RECOMMENDATIONS)
  public RecommendationResponse handle(final RecommendationRequest request) {
    var limit = request.limit();
    var tagCollections = tagCollectionFacade.findWithAnyTag(request.seedTags());
    var idsFromTagCollections = tagCollections.stream()
        .map(TagCollectionDto::id)
        .toList();

    // 1. Get all supervisors which match the seed tags
    // 2. Get all organizations which match the seed tags
    // 3. Get all projects which match the seed tags
    final var matchingSupervisors = userProfileFacade.findLecturersByIds(idsFromTagCollections);
    final var matchingOrganizations = organizationFacade.findAllByIds(idsFromTagCollections);
    final var matchingProjects = projectFacade.findAllByIds(idsFromTagCollections);

    // 4. Based on those results, calculate a confidence score for each supervisor, organization and project
    // 4.1 The overlap coefficent index can be used to calculate the confidence score for (1, 2, 3)
    //     Another option is to simply count the number of matching tags
    final Map<UUID, Double> lecturerConfidenceScoreSum = calculateLecturerConfidenceScores(
        matchingSupervisors, request.seedTags());
    final Map<UUID, Double> organizationConfidenceScoreSum = calculateOrganizationConfidenceScores(
        matchingOrganizations, request.seedTags());
    final Map<UUID, Double> projectConfidenceScoreSum = calculateProjectConfidenceScores(
        matchingProjects, request.seedTags());

    // 4.2 The confidence score for (1, 2) can be boosted by matching projects
    boostProjectParticipations(organizationConfidenceScoreSum, lecturerConfidenceScoreSum,
        projectConfidenceScoreSum, matchingProjects);

    // 5. Return the top results for each category together with the confidence score
    final var topLecturers = pickResults(lecturerConfidenceScoreSum,
        e -> new RecommendationResponse.RecommendationResult<>(e.getValue(),
            userProfileFacade.getByUserId(e.getKey()).orElse(null)))
        .filter(e -> e.item() != null && request.excludedIds().stream()
            .noneMatch(ex -> e.item().userId().equals(ex)))
        .sorted((e1, e2) -> e2.confidenceScore().compareTo(e1.confidenceScore()))
        .limit(limit)
        .toList();

    final var topOrganizations = pickResults(organizationConfidenceScoreSum,
        e -> new RecommendationResponse.RecommendationResult<>(e.getValue(),
            organizationFacade.get(e.getKey()).orElse(null)))
        .filter(e -> e.item() != null && request.excludedIds().stream()
            .noneMatch(ex -> e.item().id().equals(ex)))
        .sorted((e1, e2) -> e2.confidenceScore().compareTo(e1.confidenceScore()))
        .limit(limit)
        .toList();

    Comparator<RecommendationResult<ProjectDto>> projectComparator = Comparator.comparing(
        RecommendationResult::confidenceScore);
    projectComparator = projectComparator.thenComparing(p -> p.item().createdAt()).reversed();

    final var topProjects = pickResults(projectConfidenceScoreSum,
        e -> new RecommendationResult<>(e.getValue(),
            projectFacade.get(e.getKey()).orElse(null)))
        .filter(e -> e.item() != null && request.excludedIds().stream()
            .noneMatch(ex -> e.item().id().equals(ex)))
        .filter(e -> Arrays.stream(PROJECT_STATE_FILTER)
            .anyMatch(s -> s.equals(e.item().status().state())))
        .sorted(projectComparator)
        .limit(limit)
        .toList();

    return new RecommendationResponse(topLecturers, topOrganizations, topProjects);
  }

  private void boostProjectParticipations(
      Map<UUID, Double> organizationScores,
      Map<UUID, Double> lecturerScores,
      Map<UUID, Double> projectScores,
      final Collection<ProjectDto> matchingProjects
  ) {
    for (var entry : projectScores.entrySet()) {
      var project = matchingProjects.stream().filter(p -> p.id().equals(entry.getKey())).findFirst()
          .orElse(null);
      if (project == null) {
        continue;
      }

      var score = entry.getValue();
      var supervisors = project.supervisors();
      if (score > 0.0 && supervisors != null) {
        for (var supervisor : supervisors) {
          addConfidenceScore(lecturerScores, supervisor.id(), score, 2.0);
        }
      }

      var partner = project.partner();
      if (score > 0.0 && partner != null) {
        addConfidenceScore(organizationScores, partner.id(), score, 2.0);
      }
    }
  }

  private Map<UUID, Double> calculateOrganizationConfidenceScores(
      Collection<OrganizationDto> organizations, Collection<UUID> seedTags) {
    final Map<UUID, Double> organizationConfidenceScoreSum = new HashMap<>();

    for (var organization : organizations) {
      final var score =
          ORGANISATION_PROFILE_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags,
              organization.tags().stream().map(
                  TagDto::id).toList());
      addConfidenceScore(organizationConfidenceScoreSum, organization.id(), score);
    }

    return organizationConfidenceScoreSum;
  }

  private Map<UUID, Double> calculateLecturerConfidenceScores(Collection<UserProfileDto> lecturers,
      Collection<UUID> seedTags) {
    final Map<UUID, Double> lecturerConfidenceScoreSum = new HashMap<>();

    for (var supervisor : lecturers) {
      final var score =
          LECTURER_PROFILE_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags,
              supervisor.tags().stream().map(
                  TagDto::id).toList());
      addConfidenceScore(lecturerConfidenceScoreSum, supervisor.userId(), score);
    }

    return lecturerConfidenceScoreSum;
  }

  private Map<UUID, Double> calculateProjectConfidenceScores(Collection<ProjectDto> projects,
      Collection<UUID> seedTags) {
    final Map<UUID, Double> projectConfidenceScoreSum = new HashMap<>();

    for (var project : projects) {
      final var projectScore =
          PROJECT_MATCH_BOOST_FACTOR * overlapCoefficientCalculator.calculate(seedTags,
              project.tags().stream().map(
                  TagDto::id).toList());
      addConfidenceScore(projectConfidenceScoreSum, project.id(), projectScore);
    }

    return projectConfidenceScoreSum;
  }

  private <T> void addConfidenceScore(final Map<T, Double> map, final T obj, final double score) {
    addConfidenceScore(map, obj, score, 1.0);
  }

  private <T> void addConfidenceScore(final Map<T, Double> map, final T obj, final double score,
      final double divisor) {
    map.compute(obj, (key, value) -> {
      if (value == null) {
        return score;
      }
      return (value + score) / divisor;
    });
  }

  private <T> Stream<RecommendationResult<T>> pickResults(final Map<UUID, Double> map, final
  Function<Entry<UUID, Double>, RecommendationResult<T>> mapFn) {
    return map.entrySet().stream()
        .filter(e -> e.getValue() > 0.0)
        .map(mapFn);
  }

}
