package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.SetStateHandler;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetStateHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  SetStateHandler setStateHandler = new SetStateHandler(projectRepository);

  @Test
  void shouldThrowWhenProjectNotFound() {
    var id = UUID.randomUUID();
    when(projectRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> setStateHandler.handle(id, ProjectState.OFFERED))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldUpdateState() {
    var project = ProjectFixtures.build_a_project();
    project.offer(new Supervisor(UUID.randomUUID()));
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

    setStateHandler.handle(project.getId(), ProjectState.RUNNING);

    var captor = ArgumentCaptor.forClass(Project.class);
    verify(projectRepository).save(captor.capture());
    assertThat(captor.getValue().getStatus().getState())
        .isEqualTo(ProjectState.RUNNING);
  }

  @Test
  void shouldThrowOnInvalidTransition() {
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

    assertThatThrownBy(() -> setStateHandler.handle(project.getId(), ProjectState.STALE))
        .isInstanceOf(IllegalArgumentException.class);
  }
}