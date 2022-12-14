package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindProjectByIdHandler;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FindProjectByIdHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  FindProjectByIdHandler handler = new FindProjectByIdHandler(projectRepository);

  @Test
  void shouldFind() {
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

    var result = handler.handle(project.getId());
    assertThat(result).contains(project);
  }

  @Test
  void shouldCallRepository() {
    var id = UUID.randomUUID();
    var result = handler.handle(id);
    verify(projectRepository).findById(id);
  }
}