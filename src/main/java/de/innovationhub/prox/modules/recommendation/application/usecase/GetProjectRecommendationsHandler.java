package de.innovationhub.prox.modules.recommendation.application.usecase;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.dto.ProjectDto.ReadSupervisorDto;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationRequest;
import de.innovationhub.prox.modules.recommendation.application.dto.RecommendationResponse;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class GetProjectRecommendationsHandler {

  private final ProjectFacade projectFacade;
  private final GetRecommendationsHandler getRecommendationsHandler;

  public RecommendationResponse handle(final UUID projectId) {
    var project = projectFacade.get(projectId).orElseThrow();
    var tags = project.tags().stream().map(TagDto::id).toList();
    var excludedIds = new ArrayList<UUID>();
    excludedIds.add(project.id());
    if (project.author() != null && project.author().userId() != null) {
      excludedIds.add(project.author().userId());
    }
    if (project.partner() != null && project.partner().id() != null) {
      excludedIds.add(project.partner().id());
    }
    if (project.supervisors() != null && !project.supervisors().isEmpty()) {
      excludedIds.addAll(project.supervisors().stream().map(ReadSupervisorDto::id).toList());
    }

    var request = new RecommendationRequest(
        tags,
        excludedIds,
        5
    );

    return getRecommendationsHandler.handle(request);
  }
}
