package de.innovationhub.prox.modules.organization.application.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.OrganizationFixtures;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
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