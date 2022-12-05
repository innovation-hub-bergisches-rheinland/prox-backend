package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.SetProjectPartnerHandler;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetPartnerHandlerTest {

  ProjectRepository projectRepository = mock(ProjectRepository.class);

  SetProjectPartnerHandler setPartner = new SetProjectPartnerHandler(projectRepository);

  @Test
  void shouldSetPartner() {
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(any())).thenReturn(Optional.of(project));
    var orgId = UUID.randomUUID();

    setPartner.handle(project.getId(), orgId);

    var captor = ArgumentCaptor.forClass(Project.class);
    verify(projectRepository).save(captor.capture());

    assertThat(captor.getValue())
        .satisfies(p -> {
          assertThat(p.getPartner().getOrganizationId()).isEqualTo(orgId);
        });
  }
}