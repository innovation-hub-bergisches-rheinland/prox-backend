package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDto;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchProjectHandler {
  private final ProjectRepository projectRepository;
  private final TagFacade tagFacade;
  private final UserProfileFacade userProfileFacade;

  public Page<Project> handle(
      Collection<ProjectState> status,
      Collection<String> specializationKeys,
      Collection<String> moduleTypeKeys,
      String text,
      Collection<String> tags,
      Pageable pageable) {
    List<UUID> tagIds = new ArrayList<>();
    if(tags != null) {
      tagIds.addAll(
          tagFacade.getTagsByName(tags).stream().map(TagDto::id).toList()
      );
    }

    List<String> stateFiler = null;
    if(status != null) {
      stateFiler = status.stream().map(ProjectState::name).toList();
    }

    List<UUID> supervisorIds = new ArrayList<>();
    var profileSearch = userProfileFacade.search(text);
    if(profileSearch != null && !profileSearch.isEmpty()) {
      supervisorIds = profileSearch.stream().map(UserProfileDto::userId).toList();
    }

    return projectRepository.filterProjects(stateFiler, specializationKeys, moduleTypeKeys, text, tagIds, supervisorIds, pageable);
  }
}
