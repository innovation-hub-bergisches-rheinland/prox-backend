package de.innovationhub.prox.modules.organization.application.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.OrganizationFixtures;
import de.innovationhub.prox.modules.organization.application.dto.CreateOrganizationRequestDto;
import de.innovationhub.prox.modules.organization.application.dto.CreateOrganizationRequestDto.CreateOrganizationProfileDto;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.organization.domain.SocialMedia;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
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

    var request = new CreateOrganizationRequestDto("ACME Ltd.",
        new CreateOrganizationProfileDto(
            "2022-11-07",
            "200",
            "example.org",
            "test@example.org",
            "Lorem Ipsum",
            "Lala Land",
            "Lala Land",
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