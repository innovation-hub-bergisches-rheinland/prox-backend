package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllProjectsWithAnyTagsHandler {
  private final ProjectRepository projectRepository;

  public List<Project> handle(Collection<UUID> tags) {
    return projectRepository.findAllWithAnyTags(tags, Pageable.unpaged())
        .stream().toList();
  }
}
