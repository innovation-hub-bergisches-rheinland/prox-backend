package de.innovationhub.prox.modules.recommendation.application.usecase;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class GetRecommendationsHandler {

  private static final double LECTURER_PROFILE_MATCH_BOOST_FACTOR = 2.5;
  private static final double ORGANISATION_PROFILE_MATCH_BOOST_FACTOR = 2.5;
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
    final MultiValuedMap<UUID, Double> lecturerConfidenceScores = new ArrayListValuedHashMap<>();
    final MultiValuedMap<UUID, Double> organizationConfidenceScores = new ArrayListValuedHashMap<>();
    final MultiValuedMap<UUID, Double> projectConfidenceScores = new ArrayListValuedHashMap<>();

    for (var supervisor : matchingSupervisors) {
      final var score = overlapCoefficientCalculator.calculate(request.seedTags(),
          supervisor.tags().stream().map(
              TagDto::id).toList());
      lecturerConfidenceScores.put(supervisor.userId(),
          LECTURER_PROFILE_MATCH_BOOST_FACTOR * score);
    }

    for (var organization : matchingOrganizations) {
      final var score = overlapCoefficientCalculator.calculate(request.seedTags(),
          organization.tags().stream().map(
              TagDto::id).toList());
      organizationConfidenceScores.put(organization.id(),
          ORGANISATION_PROFILE_MATCH_BOOST_FACTOR * score);
    }

    for (var project : matchingProjects) {
      final var projectScore = overlapCoefficientCalculator.calculate(request.seedTags(),
          project.tags().stream().map(
              TagDto::id).toList());
      projectConfidenceScores.put(project.id(), projectScore);

      var supervisors = project.supervisors();
      if (supervisors != null) {
        for (var supervisor : supervisors) {
          lecturerConfidenceScores.put(supervisor.id(), projectScore);
        }
      }

      var partner = project.partner();
      if (partner != null) {
        organizationConfidenceScores.put(partner.id(), projectScore);
      }
    }

    // 5. Return the top results for each category together with the confidence score
    final var topFiveLecturers = pickResults(
        calculateAverage(lecturerConfidenceScores),
        e -> new RecommendationResponse.RecommendationResult<>(e.getValue(),
            userProfileFacade.getByUserId(e.getKey()).orElse(null)))
        .filter(e -> e.item() != null && request.excludedIds().stream().noneMatch(ex -> e.item().userId().equals(ex)))
        .sorted((e1, e2) -> e2.confidenceScore().compareTo(e1.confidenceScore()))
        .limit(5)
        .toList();

    final var topFiveOrganizations = pickResults(
        calculateAverage(organizationConfidenceScores),
        e -> new RecommendationResponse.RecommendationResult<>(e.getValue(),
            organizationFacade.get(e.getKey()).orElse(null)))
        .filter(e -> e.item() != null && request.excludedIds().stream().noneMatch(ex -> e.item().id().equals(ex)))
        .sorted((e1, e2) -> e2.confidenceScore().compareTo(e1.confidenceScore()))
        .limit(5)
        .toList();

    Comparator<RecommendationResult<ProjectDto>> projectComparator = Comparator.comparing(RecommendationResult::confidenceScore);
    projectComparator = projectComparator.thenComparing(p -> p.item().createdAt()).reversed();

    final var topFiveProjects = pickResults(
        calculateAverage(projectConfidenceScores),
        e -> new RecommendationResult<>(e.getValue(),
            projectFacade.get(e.getKey()).orElse(null)))
        .filter(e -> e.item() != null && request.excludedIds().stream().noneMatch(ex -> e.item().id().equals(ex)))
        .filter(e -> Arrays.stream(PROJECT_STATE_FILTER).anyMatch(s -> s.equals(e.item().status().state())))
        .sorted(projectComparator)
        .limit(5)
        .toList();

    return new RecommendationResponse(topFiveLecturers, topFiveOrganizations, topFiveProjects);
  }

  private HashMap<UUID, Double> calculateAverage(final MultiValuedMap<UUID, Double> map) {
    final var average = new HashMap<UUID, Double>();
    for (var entry : map.asMap().entrySet()) {
      final var sum = entry.getValue().stream().reduce(0.0, Double::sum);
      final var calculatedAverage = sum / entry.getValue().size();
      average.put(entry.getKey(), calculatedAverage);
    }
    return average;
  }

  private <T> Stream<RecommendationResult<T>> pickResults(final Map<UUID, Double> map, final
  Function<Entry<UUID, Double>, RecommendationResult<T>> mapFn) {
    return map.entrySet().stream()
        .filter(e -> e.getValue() > 0.0)
        .map(mapFn);
  }

}
