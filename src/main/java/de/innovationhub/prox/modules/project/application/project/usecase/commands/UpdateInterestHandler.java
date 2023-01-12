package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateInterestHandler {
  private final ProjectRepository projectRepository;

  public Project handle(UUID projectId, UUID userId, boolean interested) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);

    if (interested) {
      project.stateInterest(userId);
    } else {
      project.unstateInterest(userId);
    }

    return projectRepository.save(project);
  }
}
