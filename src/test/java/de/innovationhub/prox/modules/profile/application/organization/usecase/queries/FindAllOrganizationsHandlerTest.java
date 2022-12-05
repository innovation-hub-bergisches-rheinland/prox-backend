package de.innovationhub.prox.modules.profile.application.organization.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.profile.OrganizationFixtures;
import de.innovationhub.prox.modules.profile.application.organization.usecase.queries.FindAllOrganizationsHandler;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class FindAllOrganizationsHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  FindAllOrganizationsHandler handler = new FindAllOrganizationsHandler(organizationRepository);

  @Test
  void shouldReturnAllOrganizations() {
    var organizations = List.of(OrganizationFixtures.ACME_LTD);
    when(organizationRepository.findAll()).thenReturn(organizations);

    var response = handler.handle();

    assertThat(response).containsExactlyInAnyOrderElementsOf(organizations);
  }
}