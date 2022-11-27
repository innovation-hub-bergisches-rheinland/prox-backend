package de.innovationhub.prox.modules.project.application.project.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ApplyCommitmentHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
  ApplyCommitmentHandler applyCommitmentHandler = new ApplyCommitmentHandler(projectRepository, authenticationFacade);

  @Test
  void shouldApplyCommitment() {
    var userId = UUID.randomUUID();
    when(authenticationFacade.currentAuthenticated()).thenReturn(userId);
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
    when(projectRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var response = applyCommitmentHandler.handle(project.getId());

    assertThat(response.getSupervisors())
        .hasSize(1)
        .extracting(Supervisor::getLecturerId)
        .containsExactly(userId);
    assertThat(response.getStatus().getState())
        .isEqualTo(ProjectState.OFFERED);
  }

  @Test
  void shouldNotApplyCommitment() {
    var userId = UUID.randomUUID();
    when(authenticationFacade.currentAuthenticated()).thenReturn(userId);
    var project = ProjectFixtures.build_a_project();
    project.offer(new Supervisor(UUID.randomUUID()));
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

    assertThatThrownBy(() -> applyCommitmentHandler.handle(project.getId()))
        .isInstanceOf(RuntimeException.class);
  }
}