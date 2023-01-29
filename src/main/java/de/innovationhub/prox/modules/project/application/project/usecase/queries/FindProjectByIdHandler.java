package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindProjectByIdHandler {
  private final ProjectRepository projectRepository;

  public Optional<Project> handle(UUID id) {
    return projectRepository.findById(id);
  }
}
