package de.innovationhub.prox.modules.project.application.project.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetProjectTagsHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  SetProjectTagsHandler setProjectTagsHandler = new SetProjectTagsHandler(projectRepository);

  @Test
  void shouldThrowWhenProjectNotFound() {
    var id = UUID.randomUUID();
    when(projectRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> setProjectTagsHandler.handle(id, List.of()))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldSaveWithTags() {
    var project = ProjectFixtures.build_a_project();
    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());
    when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

    setProjectTagsHandler.handle(project.getId(), tags);

    var captor = ArgumentCaptor.forClass(Project.class);
    verify(projectRepository).save(captor.capture());
    assertThat(captor.getValue().getTags()).containsExactlyInAnyOrderElementsOf(tags);
  }
}