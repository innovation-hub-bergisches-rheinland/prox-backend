package de.innovationhub.prox.modules.organization.application.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.OrganizationFixtures;
import de.innovationhub.prox.modules.organization.application.dto.AddMembershipRequestDto;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class AddOrganizationMemberHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

  AddOrganizationMemberHandler handler = new AddOrganizationMemberHandler(organizationRepository,
      authenticationFacade);

  @Test
  void shouldThrowWhenOrganizationNotFound() {
    when(organizationRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), null));
  }

  @Test
  void shouldThrowWhenUserNotMember() {
    var org = OrganizationFixtures.ACME_LTD;
    when(organizationRepository.findById(org.getId())).thenReturn(Optional.of(org));
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(UUID.randomUUID());

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), null));
  }

  @Test
  void shouldAddMember() {
    var org = OrganizationFixtures.ACME_LTD;
    when(organizationRepository.findById(org.getId())).thenReturn(Optional.of(org));
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(OrganizationFixtures.ACME_ADMIN);

    var userId = UUID.randomUUID();
    var request = new AddMembershipRequestDto(userId, OrganizationRole.MEMBER);

    var membership = handler.handle(org.getId(), request);
    assertThat(membership.getMemberId()).isEqualTo(userId);
    assertThat(membership.getRole()).isEqualTo(OrganizationRole.MEMBER);

    var captor = ArgumentCaptor.forClass(Organization.class);
    verify(organizationRepository).save(captor.capture());
    var saved = captor.getValue();
    assertThat(saved.isInRole(userId, OrganizationRole.MEMBER)).isTrue();
  }
}