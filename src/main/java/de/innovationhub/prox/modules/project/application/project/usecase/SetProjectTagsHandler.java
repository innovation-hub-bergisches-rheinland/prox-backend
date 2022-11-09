package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetProjectTagsHandler {
  private final ProjectRepository projectRepository;

  public List<UUID> handle(UUID projectId,
      List<UUID> tags) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(() -> new RuntimeException("Project not found")); // TODO: Create custom exception
    project.setTags(tags);
    projectRepository.save(project);
    return List.copyOf(project.getTags());
  }
}
