package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class FindAllProjectsHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  FindAllProjectsHandler findAllProjectsHandler = new FindAllProjectsHandler(projectRepository);

  @Test
  void shouldFindAll() {
    var projects = ProjectFixtures.build_project_list();
    var page = PageRequest.of(0, 10);
    PageImpl<Project> pageImpl = new PageImpl<>(projects, page, projects.size());
    when(projectRepository.findAll(page)).thenReturn(pageImpl);

    var result = findAllProjectsHandler.handle(page);

    assertThat(result).containsExactlyInAnyOrderElementsOf(projects);
  }
}