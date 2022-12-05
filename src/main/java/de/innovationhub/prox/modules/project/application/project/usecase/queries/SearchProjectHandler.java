package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchProjectHandler {
  private final ProjectRepository projectRepository;

  public List<Project> handle(ProjectState status,
      Collection<String> specializationKeys,
      Collection<String> moduleTypeKeys,
      String text) {
    return projectRepository.filterProjects(status, specializationKeys, moduleTypeKeys, text);
  }
}
