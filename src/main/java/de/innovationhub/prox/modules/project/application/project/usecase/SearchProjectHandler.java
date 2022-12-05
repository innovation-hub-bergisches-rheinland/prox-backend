package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchProjectHandler {
  private final ProjectRepository projectRepository;

  public Page<Project> handle(ProjectState status,
      Collection<String> specializationKeys,
      Collection<String> moduleTypeKeys,
      String text,
      Pageable pageable) {
    return projectRepository.filterProjects(status, specializationKeys, moduleTypeKeys, text, pageable);
  }
}
