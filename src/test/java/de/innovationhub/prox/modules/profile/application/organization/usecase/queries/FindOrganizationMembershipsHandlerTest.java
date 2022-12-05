package de.innovationhub.prox.modules.profile.application.organization.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.profile.OrganizationFixtures;
import de.innovationhub.prox.modules.profile.application.organization.usecase.queries.FindOrganizationMembershipsHandler;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class FindOrganizationMembershipsHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  FindOrganizationMembershipsHandler handler = new FindOrganizationMembershipsHandler(organizationRepository);

  @Test
  void shouldReturnOrganizationMemberships() {
    var org = OrganizationFixtures.ACME_LTD;

    when(organizationRepository.findById(org.getId())).thenReturn(Optional.of(org));

    var response = handler.handle(org.getId());
    assertThat(response)
        .containsExactlyInAnyOrderElementsOf(org.getMembers());
  }
}