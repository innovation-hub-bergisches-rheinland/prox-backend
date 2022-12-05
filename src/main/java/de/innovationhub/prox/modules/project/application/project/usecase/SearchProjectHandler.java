package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchProjectHandler {

  private final ProjectRepository projectRepository;

  public List<Project> handle(ProjectState status,
      Collection<String> specializationKeys,
      Collection<String> moduleTypeKeys,
      String text) {
    // Fallback to findAll if no filter is set.
    if (status == null
        && (specializationKeys == null || specializationKeys.isEmpty())
        && (moduleTypeKeys == null || moduleTypeKeys.isEmpty())
        && text == null
    ) {
      return StreamSupport.stream(projectRepository.findAll().spliterator(), false)
          .toList();
    }
    return projectRepository.filterProjects(status, specializationKeys, moduleTypeKeys, text);
  }
}
