package de.innovationhub.prox.modules.project.application.project.usecase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.List;
import org.junit.jupiter.api.Test;

class SearchProjectHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  SearchProjectHandler searchProjectHandler = new SearchProjectHandler(projectRepository);

  @Test
  void shouldCallRepository() {
    var status = ProjectState.STALE;
    var keys = List.of("key");
    var modules = List.of("module");
    var text = "lol";

    searchProjectHandler.handle(status, keys, modules, text);

    verify(projectRepository).filterProjects(status, keys, modules, text);
  }

  @Test
  void shouldFallbackToFindAllOnNulls() {
    searchProjectHandler.handle(null, null, null, null);

    verify(projectRepository).findAll();
  }

  @Test
  void shouldFallbackToFindAllOnEmptyLists() {
    searchProjectHandler.handle(null, List.of(), List.of(), null);

    verify(projectRepository).findAll();
  }
}