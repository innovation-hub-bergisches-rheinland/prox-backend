package de.innovationhub.prox.modules.organization.application.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.OrganizationFixtures;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.user.contract.account.AuthenticationFacade;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class RemoveOrganizationMemberHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

  RemoveOrganizationMemberHandler handler = new RemoveOrganizationMemberHandler(organizationRepository,
      authenticationFacade);

  @Test
  void shouldThrowWhenOrganizationNotFound() {
    when(organizationRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), null));
  }

  @Test
  void shouldThrowWhenUserNotAdmin() {
    var org = OrganizationFixtures.ACME_LTD;
    var userId = UUID.randomUUID();
    org.addMember(userId, OrganizationRole.MEMBER);
    when(organizationRepository.findById(org.getId())).thenReturn(Optional.of(org));
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(userId);

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), null));
  }

  @Test
  void shouldRemoveMember() {
    var org = OrganizationFixtures.ACME_LTD;
    var userId = UUID.randomUUID();
    org.addMember(userId, OrganizationRole.MEMBER);
    when(organizationRepository.findById(org.getId())).thenReturn(Optional.of(org));
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(OrganizationFixtures.ACME_ADMIN);

    handler.handle(org.getId(), userId);

    var captor = ArgumentCaptor.forClass(Organization.class);
    verify(organizationRepository).save(captor.capture());
    assertThat(captor.getValue().getMembers())
        .filteredOn(m -> m.getMemberId().equals(userId))
        .isEmpty();
  }


}