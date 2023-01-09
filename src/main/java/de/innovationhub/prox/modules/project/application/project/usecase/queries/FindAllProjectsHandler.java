package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllProjectsHandler {
  private final ProjectRepository projectRepository;

  public Page<Project> handle(Pageable pageable) {
    return projectRepository.findAll(pageable);
  }
}
