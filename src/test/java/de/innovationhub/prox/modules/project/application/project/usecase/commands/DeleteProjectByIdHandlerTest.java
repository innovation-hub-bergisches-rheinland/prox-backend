package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.user.contract.AuthenticationFacade;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeleteProjectByIdHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
  DeleteProjectByIdHandler deleteProjectByIdHandler = new DeleteProjectByIdHandler(projectRepository, authenticationFacade);

  @Test
  void shouldThrowWhenNotFound() {
    when(projectRepository.findById(any())).thenReturn(Optional.empty());

    var id = UUID.randomUUID();
    assertThrows(RuntimeException.class, () -> deleteProjectByIdHandler.handle(id));
  }

  @Test
  void shouldDelete() {
    var project = ProjectFixtures.build_a_project();
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(ProjectFixtures.A_PROJECT_CREATOR_ID);
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

    deleteProjectByIdHandler.handle(project.getId());

    verify(projectRepository).delete(project);
  }
}