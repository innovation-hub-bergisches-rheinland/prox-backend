package de.innovationhub.prox.modules.project.application;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindAllProjectsWithAnyTagsHandler;
import de.innovationhub.prox.modules.project.contract.ProjectFacade;
import de.innovationhub.prox.modules.project.contract.ProjectViewMapper;
import de.innovationhub.prox.modules.project.contract.ProjectView;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class ProjectFacadeImpl implements ProjectFacade {
  private final FindAllProjectsWithAnyTagsHandler findAllProjectsWithAnyTagsHandler;
  private final ProjectViewMapper projectViewMapper;

  @Override
  public List<ProjectView> findAllWithAnyTags(List<UUID> tags) {
    return findAllProjectsWithAnyTagsHandler.handle(tags)
        .stream()
        .map(projectViewMapper::toView)
        .toList();
  }
}
