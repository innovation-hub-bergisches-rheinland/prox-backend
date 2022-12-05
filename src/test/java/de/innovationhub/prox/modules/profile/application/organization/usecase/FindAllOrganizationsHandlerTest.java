package de.innovationhub.prox.modules.profile.application.organization.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.profile.OrganizationFixtures;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class FindAllOrganizationsHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  FindAllOrganizationsHandler handler = new FindAllOrganizationsHandler(organizationRepository);

  @Test
  void shouldReturnAllOrganizations() {
    var organizations = List.of(OrganizationFixtures.ACME_LTD);
    when(organizationRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(organizations));

    var response = handler.handle(Pageable.unpaged());

    assertThat(response).containsExactlyInAnyOrderElementsOf(organizations);
  }
}