package de.innovationhub.prox.modules.profile.application.organization.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.profile.application.organization.usecase.commands.CreateOrganizationHandler;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.CreateOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.CreateOrganizationDto.CreateOrganizationProfileDto;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import de.innovationhub.prox.modules.profile.domain.organization.SocialMedia;
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

    var request = new CreateOrganizationDto("ACME Ltd.",
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