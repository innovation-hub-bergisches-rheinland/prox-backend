package de.innovationhub.prox.modules.project.application.project.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FindProjectByIdHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  FindProjectByIdHandler handler = new FindProjectByIdHandler(projectRepository);

  @Test
  void shouldFind() {
    var project = ProjectFixtures.A_PROJECT;
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