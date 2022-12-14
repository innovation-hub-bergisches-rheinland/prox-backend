package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

class SearchProjectHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  SearchProjectHandler searchProjectHandler = new SearchProjectHandler(projectRepository);

  @Test
  void shouldCallRepository() {
    var status = ProjectState.STALE;
    var keys = List.of("key");
    var modules = List.of("module");
    var text = "lol";
    var page = PageRequest.of(0, 10);

    searchProjectHandler.handle(status, keys, modules, text, page);

    verify(projectRepository).filterProjects(status, keys, modules, text, page);
  }
}