package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
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
  public List<UUID> handle(UUID projectId,
      List<UUID> tags) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);
    project.setTags(tags);
    projectRepository.save(project);
    return List.copyOf(project.getTags());
  }
}
