package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.TagView;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class SearchProjectHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  TagFacade tagFacade = mock(TagFacade.class);
  SearchProjectHandler searchProjectHandler = new SearchProjectHandler(projectRepository, tagFacade);

  @Test
  void shouldCallRepository() {
    var status = List.of(ProjectState.STALE);
    var keys = List.of("key");
    var modules = List.of("module");
    var text = "lol";
    var page = PageRequest.of(0, 10);

    searchProjectHandler.handle(status, keys, modules, text, null, page);

    verify(projectRepository).filterProjects(eq(List.of("STALE")), eq(keys), eq(modules), eq(text), anyCollection(), eq(page));
  }

  @Test
  void shouldCallRepositoryWithResolvedTags() {
    var givenTags = List.of(
        new TagView(UUID.randomUUID(), "tag1")
    );
    when(tagFacade.getTagsByName(anyCollection())).thenReturn(givenTags);

    searchProjectHandler.handle(null, null, null, null, List.of("tag1"), Pageable.unpaged());

    ArgumentCaptor<List<UUID>> captor = ArgumentCaptor.forClass((Class) List.class);
    verify(projectRepository).filterProjects(any(), any(), any(), any() , captor.capture(), any());
    assertThat(captor.getValue()).containsExactly(givenTags.get(0).id());
  }
}