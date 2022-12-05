package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.SetSupervisorsHandler;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SetSupervisorsHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  SetSupervisorsHandler setSupervisorsHandler = new SetSupervisorsHandler(projectRepository);

  @Test
  void shouldApplyCommitment() {
    var userId = UUID.randomUUID();
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
    when(projectRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var response = setSupervisorsHandler.handle(project.getId(), List.of(userId));

    assertThat(response.getSupervisors())
        .hasSize(1)
        .extracting(Supervisor::getLecturerId)
        .containsExactly(userId);
    assertThat(response.getStatus().getState())
        .isEqualTo(ProjectState.OFFERED);
  }
}