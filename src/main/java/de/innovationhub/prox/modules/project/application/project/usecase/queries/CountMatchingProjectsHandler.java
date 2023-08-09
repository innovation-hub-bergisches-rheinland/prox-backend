package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class CountMatchingProjectsHandler {

  private final ProjectRepository projectRepository;
  private final TagCollectionFacade tagCollectionFacade;

  public int handle(
      Collection<ProjectState> status,
      Collection<String> specializationKeys,
      Collection<String> moduleTypeKeys,
      String text,
      Collection<UUID> tagIds,
      Instant modifiedSince) {
    List<UUID> tagCollectionIds = new ArrayList<>();
    if (tagIds != null) {
      tagCollectionIds.addAll(tagCollectionFacade.findWithAllTags(tagIds).stream().map(
          TagCollectionDto::id).toList());
    }

    List<String> stateFiler = null;
    if (status != null) {
      stateFiler = status.stream().map(ProjectState::name).toList();
    }

    return projectRepository.filterProjects(stateFiler, specializationKeys, moduleTypeKeys, text,
        tagCollectionIds, List.of(), modifiedSince, Pageable.ofSize(1)).getNumberOfElements();
  }
}
