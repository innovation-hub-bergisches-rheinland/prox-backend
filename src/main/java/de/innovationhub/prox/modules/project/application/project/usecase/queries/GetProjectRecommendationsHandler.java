package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import de.innovationhub.prox.modules.recommendation.contract.RecommendationFacade;
import de.innovationhub.prox.modules.recommendation.contract.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.contract.RecommendationResponse;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class GetProjectRecommendationsHandler {

  private final RecommendationFacade recommendationFacade;
  private final ProjectRepository projectRepository;
  private final TagCollectionFacade tagCollectionFacade;

  public RecommendationResponse handle(final UUID projectId) {
    var project = projectRepository.findById(projectId).orElseThrow();
    var tags = tagCollectionFacade.getTagCollection(project.getTagCollectionId())
        .map(tagCollection -> tagCollection.tags().stream().map(TagDto::id).toList())
        .orElseGet(List::of);
    var excludedIds = new ArrayList<UUID>();
    excludedIds.add(project.getId());
    excludedIds.add(project.getAuthor().getUserId());
    if (project.getPartner() != null) {
      excludedIds.add(project.getPartner().getOrganizationId());
    }
    if (project.getSupervisors() != null && !project.getSupervisors().isEmpty()) {
      excludedIds.addAll(project.getSupervisors().stream().map(Supervisor::getLecturerId).toList());
    }

    var request = new RecommendationRequest(
        tags,
        excludedIds,
        5
    );

    return recommendationFacade.getRecommendations(request);
  }
}
