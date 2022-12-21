package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class ApplyCommitmentHandler {
  private final ProjectRepository projectRepository;

  @PreAuthorize("hasRole('professor')")
  public Project handle(UUID projectId, UUID supervisor) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);

    project.offer(new Supervisor(supervisor));

    return projectRepository.save(project);
  }
}
