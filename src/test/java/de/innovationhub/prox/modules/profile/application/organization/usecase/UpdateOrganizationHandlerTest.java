package de.innovationhub.prox.modules.profile.application.organization.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.profile.OrganizationFixtures;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.UpdateOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.UpdateOrganizationDto.UpdateOrganizationProfileDto;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import de.innovationhub.prox.modules.profile.domain.organization.SocialMedia;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateOrganizationHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

  UpdateOrganizationHandler handler = new UpdateOrganizationHandler(organizationRepository,
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
  void shouldUpdateOrganization() {
    var org = OrganizationFixtures.ACME_LTD;
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(OrganizationFixtures.ACME_ADMIN);
    when(organizationRepository.findById(org.getId())).thenReturn(Optional.of(org));

    var request = new UpdateOrganizationDto("ACME Ltd.",
        new UpdateOrganizationProfileDto(
            "2022-11-07",
            "200",
            "example.org",
            "test@example.org",
            "Lorem Ipsum",
            "Lala Land",
            List.of(),
            Map.of(SocialMedia.FACEBOOK, "acmeltd")
        ));

    handler.handle(org.getId(), request);

    var captor = ArgumentCaptor.forClass(Organization.class);
    verify(organizationRepository).save(captor.capture());
    var saved = captor.getValue();

    assertThat(saved.getName()).isEqualTo("ACME Ltd.");
    assertThat(saved.getProfile().getFoundingDate()).isEqualTo("2022-11-07");
    assertThat(saved.getProfile().getNumberOfEmployees()).isEqualTo("200");
    assertThat(saved.getProfile().getHomepage()).isEqualTo("example.org");
    assertThat(saved.getProfile().getContactEmail()).isEqualTo("test@example.org");
    assertThat(saved.getProfile().getVita()).isEqualTo("Lorem Ipsum");
    assertThat(saved.getProfile().getHeadquarter()).isEqualTo("Lala Land");
    assertThat(saved.getProfile().getSocialMediaHandles()).containsEntry(SocialMedia.FACEBOOK, "acmeltd");
  }

}