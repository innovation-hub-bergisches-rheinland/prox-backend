package de.innovationhub.prox.modules.project.application.project.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class FindAllProjectsHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  FindAllProjectsHandler findAllProjectsHandler = new FindAllProjectsHandler(projectRepository);

  @Test
  void shouldFindAll() {
    var projects = ProjectFixtures.build_project_list();
    when(projectRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(projects));

    var result = findAllProjectsHandler.handle(Pageable.unpaged());

    assertThat(result).containsExactlyInAnyOrderElementsOf(projects);
  }
}