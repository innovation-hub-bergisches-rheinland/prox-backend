package de.innovationhub.prox.modules.profile.application.organization.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.profile.OrganizationFixtures;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetOrganizationLogoHandlerTest {
  StorageProvider storageProvider = mock(StorageProvider.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  SetOrganizationLogoHandler handler = new SetOrganizationLogoHandler(storageProvider, authenticationFacade, organizationRepository);

  @Test
  void shouldThrowWhenOrganizationNotFound() {
    when(organizationRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), new byte[] {}, ""));
  }

  @Test
  void shouldSetOrganizationLogo() throws IOException {
    var org = OrganizationFixtures.ACME_LTD;
    when(organizationRepository.findById(any())).thenReturn(Optional.of(org));
    when(authenticationFacade.currentAuthenticated()).thenReturn(OrganizationFixtures.ACME_ADMIN);

    var content = new byte[] { 1, 2, 3 };
    var contentType = "image/test";
    handler.handle(UUID.randomUUID(), content, contentType);

    var fileIdCapture = ArgumentCaptor.forClass(String.class);
    verify(storageProvider).storeFile(fileIdCapture.capture(), aryEq(content), eq(contentType));

    var orgCaptor = ArgumentCaptor.forClass(Organization.class);
    verify(organizationRepository).save(orgCaptor.capture());

    assertThat(orgCaptor.getValue())
        .satisfies(l -> assertThat(l.getLogoKey()).isEqualTo(fileIdCapture.getValue()));
  }
}