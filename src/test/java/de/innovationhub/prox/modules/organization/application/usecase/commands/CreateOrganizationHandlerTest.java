package de.innovationhub.prox.modules.organization.application.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.application.dto.CreateOrganizationRequestDto;
import de.innovationhub.prox.modules.organization.application.dto.CreateOrganizationRequestDto.CreateOrganizationProfileDto;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.organization.domain.SocialMedia;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CreateOrganizationHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

  CreateOrganizationHandler handler = new CreateOrganizationHandler(organizationRepository,
      authenticationFacade);

  @Test
  void shouldCreateNewOrganization() {
    var userId = UUID.randomUUID();
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(userId);

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

    handler.handle(request);

    var captor = ArgumentCaptor.forClass(Organization.class);
    verify(organizationRepository).save(captor.capture());
    var saved = captor.getValue();

    assertThat(saved.getName()).isEqualTo("ACME Ltd.");
    assertThat(saved.isInRole(userId, OrganizationRole.ADMIN)).isTrue();
    assertThat(saved.getProfile().getFoundingDate()).isEqualTo("2022-11-07");
    assertThat(saved.getProfile().getNumberOfEmployees()).isEqualTo("200");
    assertThat(saved.getProfile().getHomepage()).isEqualTo("example.org");
    assertThat(saved.getProfile().getContactEmail()).isEqualTo("test@example.org");
    assertThat(saved.getProfile().getVita()).isEqualTo("Lorem Ipsum");
    assertThat(saved.getProfile().getHeadquarter()).isEqualTo("Lala Land");
    assertThat(saved.getProfile().getSocialMediaHandles()).containsEntry(SocialMedia.FACEBOOK, "acmeltd");
  }

}