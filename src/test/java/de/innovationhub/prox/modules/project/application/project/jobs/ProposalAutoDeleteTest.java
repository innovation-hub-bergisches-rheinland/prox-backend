package de.innovationhub.prox.modules.project.application.project.jobs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ProposalAutoDeleteTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  ProposalAutoDelete handler = new ProposalAutoDelete(projectRepository);

  @Test
  void shouldArchiveInactiveProposals() {
    var project = ProjectFixtures.build_a_project();
    project.archive();
    project.stale();
    when(projectRepository.findWithStatusModifiedBefore(any(), any()))
        .thenReturn(List.of(project));

    handler.run();

    @SuppressWarnings("unchecked")
    ArgumentCaptor<Iterable<Project>> iterableArgumentCaptor = ArgumentCaptor.forClass(Iterable.class);
    verify(projectRepository).deleteAll(iterableArgumentCaptor.capture());
    assertThat(iterableArgumentCaptor.getValue())
        .hasSize(1)
        .first()
        .isEqualTo(project);
  }
}