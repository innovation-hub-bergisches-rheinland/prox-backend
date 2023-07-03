package de.innovationhub.prox.modules.recommendation.application.usecase;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto.ReadSupervisorDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse.RecommendationResult;
import de.innovationhub.prox.modules.recommendation.domain.ConfidenceScoreCalculator;
import de.innovationhub.prox.modules.recommendation.domain.LecturerRecommendation;
import de.innovationhub.prox.modules.recommendation.domain.OrganizationRecommendation;
import de.innovationhub.prox.modules.recommendation.domain.ProjectRecommendation;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
class GetRecommendationsHandler {

  private static final ProjectState[] PROJECT_STATE_FILTER = {
      ProjectState.OFFERED, ProjectState.PROPOSED, ProjectState.RUNNING
  };

  private final TagCollectionFacade tagCollectionFacade;
  private final UserProfileFacade userProfileFacade;
  private final OrganizationFacade organizationFacade;
  private final ProjectFacade projectFacade;

  @Cacheable(CacheConfig.RECOMMENDATIONS)
  @Transactional
  public RecommendationResponse handle(final RecommendationRequest request) {
    var limit = request.limit();
    var tagCollections = tagCollectionFacade.findWithAnyTag(request.seedTags());
    var idsFromTagCollections = tagCollections.stream()
        .map(TagCollectionDto::id)
        .toList();

    // 1. Get all supervisors which match the seed tags
    // 2. Get all organizations which match the seed tags
    // 3. Get all projects which match the seed tags
    final var matchingSupervisors = userProfileFacade.findLecturersByIds(idsFromTagCollections)
        .stream()
        .map(l -> new LecturerRecommendation(l.userId(), l.tags().stream().map(TagDto::id).collect(
            Collectors.toSet()))).toList();
    final var matchingOrganizations = organizationFacade.findAllByIds(idsFromTagCollections)
        .stream()
        .map(o -> new OrganizationRecommendation(o.id(), o.tags().stream().map(TagDto::id).collect(
            Collectors.toSet()))).toList();
    final var matchingProjects = projectFacade.findAllByIds(idsFromTagCollections)
        .stream()
        .map(p -> new ProjectRecommendation(p.id(),
            p.supervisors().stream().map(ReadSupervisorDto::id)
                .collect(Collectors.toSet()), p.partner() != null ? p.partner().id() : null,
            p.tags().stream().map(TagDto::id).collect(
                Collectors.toSet()))).toList();

    // 4. Based on those results, calculate a confidence score for each supervisor, organization and project
    final var confidenceScoreCalculator = ConfidenceScoreCalculator.builder()
        .withSeedTags(request.seedTags())
        .withSeedLecturers(matchingSupervisors)
        .withSeedOrganizations(matchingOrganizations)
        .withSeedProjects(matchingProjects)
        .build();

    // 5. Return the top results for each category together with the confidence score
    final var topLecturers = pickResults(confidenceScoreCalculator.getLecturerConfidenceScores(),
        userProfileFacade::getByUserId, UserProfileDto::userId, request.excludedIds())
        .sorted((e1, e2) -> e2.confidenceScore().compareTo(e1.confidenceScore()))
        .limit(limit)
        .toList();

    final var topOrganizations = pickResults(
        confidenceScoreCalculator.getOrganizationConfidenceScores(),
        organizationFacade::get, OrganizationDto::id, request.excludedIds())
        .toList();

    Comparator<RecommendationResult<ProjectDto>> projectComparator = Comparator.comparing(
        RecommendationResult::confidenceScore);
    projectComparator = projectComparator.thenComparing(p -> p.item().createdAt()).reversed();

    final var topProjects = pickResults(confidenceScoreCalculator.getProjectConfidenceScores(),
        projectFacade::get, ProjectDto::id, request.excludedIds())
        .filter(e -> Arrays.stream(PROJECT_STATE_FILTER)
            .anyMatch(s -> s.equals(e.item().status().state())))
        .sorted(projectComparator)
        .limit(limit)
        .toList();

    return new RecommendationResponse(topLecturers, topOrganizations, topProjects);
  }

  private <T> Stream<RecommendationResult<T>> pickResults(
      final Map<UUID, Double> map,
      final Function<UUID, Optional<T>> mapFn,
      final Function<T, UUID> idExtractor,
      final List<UUID> excludedIds) {
    return map.entrySet().stream()
        .filter(e -> e.getValue() > 0.0)
        .map(e -> new RecommendationResult<>(e.getValue(), mapFn.apply(e.getKey()).orElse(null)))
        .filter(e -> e.item() != null)
        .filter(e -> excludedIds.stream().noneMatch(ex -> ex.equals(idExtractor.apply(e.item()))));
  }

}
