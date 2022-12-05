package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindProjectsOfSupervisorHandler {
  private final ProjectRepository projectRepository;

  public List<Project> handle(UUID supervisor) {
    return projectRepository.findBySupervisor(supervisor);
  }
}
