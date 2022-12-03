package de.innovationhub.prox.modules.project.application.project.usecase;

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
public class SetSupervisorsHandler {
  private final ProjectRepository projectRepository;

  @PreAuthorize("hasRole('professor')")
  public Project handle(UUID projectId, List<UUID> supervisors) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);

    var supervisorList = supervisors.stream()
        .map(Supervisor::new)
        .toList();
    project.offer(supervisorList);

    return projectRepository.save(project);
  }
}
