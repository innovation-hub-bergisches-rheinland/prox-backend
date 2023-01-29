package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetStateHandler {
  private final ProjectRepository projectRepository;

  public Project handle(UUID projectId, ProjectState state) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);

    project.updateState(state);
    projectRepository.save(project);

    return project;
  }
}
