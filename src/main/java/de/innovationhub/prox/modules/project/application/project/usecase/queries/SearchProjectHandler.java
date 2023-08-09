package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.contract.event.ProjectSearched;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchProjectHandler {

  private final ProjectRepository projectRepository;
  private final TagFacade tagFacade;
  private final UserProfileFacade userProfileFacade;
  private final TagCollectionFacade tagCollectionFacade;
  private final ApplicationEventPublisher eventPublisher;

  public Page<Project> handle(
      Collection<ProjectState> status,
      Collection<String> specializationKeys,
      Collection<String> moduleTypeKeys,
      String text,
      Collection<String> tags,
      Pageable pageable) {
    List<UUID> tagCollectionIds = new ArrayList<>();
    var tagIds = tagFacade.getTagsByName(tags).stream().map(TagDto::id).toList();
    if (tags != null) {
      tagCollectionIds.addAll(tagCollectionFacade.findWithAllTags(tagIds).stream().map(
          TagCollectionDto::id).toList());
    }

    List<String> stateFiler = null;
    if (status != null) {
      stateFiler = status.stream().map(ProjectState::name).toList();
    }

    List<UUID> supervisorIds = new ArrayList<>();
    var profileSearch = userProfileFacade.search(text);
    if (profileSearch != null && !profileSearch.isEmpty()) {
      supervisorIds = profileSearch.stream().map(UserProfileDto::userId).toList();
    }

    eventPublisher.publishEvent(new ProjectSearched(
        text, status, specializationKeys, moduleTypeKeys, tagIds
    ));

    return projectRepository.filterProjects(stateFiler, specializationKeys, moduleTypeKeys, text,
        tagCollectionIds, supervisorIds, pageable);
  }
}
