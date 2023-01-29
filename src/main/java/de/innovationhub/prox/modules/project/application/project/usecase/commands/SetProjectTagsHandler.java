package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class SetProjectTagsHandler {
  private final ProjectRepository projectRepository;

  @PreAuthorize("@projectPermissionEvaluator.hasPermission(#projectId, authentication)")
  public Project handle(UUID projectId,
      List<UUID> tags) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);
    project.setTags(tags);
    projectRepository.save(project);
    return project;
  }
}
