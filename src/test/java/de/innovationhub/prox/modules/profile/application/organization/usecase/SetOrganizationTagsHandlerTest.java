package de.innovationhub.prox.modules.profile.application.organization.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.profile.OrganizationFixtures;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetOrganizationTagsHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  AuthenticationFacade authentication = mock(AuthenticationFacade.class);
  SetOrganizationTagsHandler handler = new SetOrganizationTagsHandler(organizationRepository, authentication);

  @Test
  void shouldThrowWhenOrganizationNotFound() {
    when(organizationRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), List.of()));
  }

  @Test
  void shouldSetTags() {
    var org = OrganizationFixtures.ACME_LTD;
    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());
    when(organizationRepository.findById(any())).thenReturn(Optional.of(org));
    when(authentication.currentAuthenticatedId()).thenReturn(OrganizationFixtures.ACME_ADMIN);

    handler.handle(org.getId(), tags);

    var orgCaptor = ArgumentCaptor.forClass(Organization.class);
    verify(organizationRepository).save(orgCaptor.capture());
    assertThat(orgCaptor.getValue().getTags()).containsExactlyInAnyOrderElementsOf(tags);
  }
}