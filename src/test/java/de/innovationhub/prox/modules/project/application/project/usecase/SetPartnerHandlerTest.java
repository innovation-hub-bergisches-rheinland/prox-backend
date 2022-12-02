package de.innovationhub.prox.modules.project.application.project.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.profile.contract.OrganizationFacade;
import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetPartnerHandlerTest {

  ProjectRepository projectRepository = mock(ProjectRepository.class);
  OrganizationFacade organizationFacade = mock(OrganizationFacade.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

  SetProjectPartnerHandler setPartner = new SetProjectPartnerHandler(projectRepository, organizationFacade, authenticationFacade);

  @Test
  void shouldSetPartner() {
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(any())).thenReturn(Optional.of(project));
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(UUID.randomUUID());
    when(organizationFacade.isMember(any(), any())).thenReturn(true);
    var orgId = UUID.randomUUID();

    setPartner.handle(project.getId(), orgId);

    var captor = ArgumentCaptor.forClass(Project.class);
    verify(projectRepository).save(captor.capture());

    assertThat(captor.getValue())
        .satisfies(p -> {
          assertThat(p.getPartner().getOrganizationId()).isEqualTo(orgId);
        });
  }

  @Test
  void shouldThrowWhenNotMember() {
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(any())).thenReturn(Optional.of(project));
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(UUID.randomUUID());
    when(organizationFacade.isMember(any(), any())).thenReturn(false);
    var orgId = UUID.randomUUID();

    assertThatThrownBy(() -> setPartner.handle(project.getId(), orgId))
        .isInstanceOf(IllegalArgumentException.class);
  }
}