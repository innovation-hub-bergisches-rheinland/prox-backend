package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class SetStateHandler {
  private final ProjectRepository projectRepository;

  @PreAuthorize("@projectPermissionEvaluator.hasPermission(#projectId, authentication)")
  public Project handle(UUID projectId, ProjectState state) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);

    switch (state) {
      case RUNNING -> project.start();
      case COMPLETED -> project.complete();
      case OFFERED -> project.offer(project.getSupervisors());
      default -> throw new IllegalArgumentException("Invalid state: " + state);
    }

    projectRepository.save(project);
    return project;
  }
}
