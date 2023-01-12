package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.InterestedUser;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UpdateInterestHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  UpdateInterestHandler handler = new UpdateInterestHandler(projectRepository);

  @Test
  void shouldRegisterInterest() {
    var userId = UUID.randomUUID();
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
    when(projectRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var response = handler.handle(project.getId(), userId, true);

    assertThat(response.getInterestedUsers())
        .hasSize(1)
        .extracting(InterestedUser::getUserId)
        .containsExactly(userId);
  }

  @Test
  void shouldRemoveInterest() {
    var userId = UUID.randomUUID();
    var project = ProjectFixtures.build_a_project();
    project.stateInterest(new InterestedUser(userId));
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
    when(projectRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var response = handler.handle(project.getId(), userId, false);

    assertThat(response.getInterestedUsers())
        .extracting(InterestedUser::getUserId)
        .doesNotContain(userId);
  }
}