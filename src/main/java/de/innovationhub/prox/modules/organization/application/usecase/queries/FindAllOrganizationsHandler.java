package de.innovationhub.prox.modules.organization.application.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@ApplicationComponent
public class FindAllOrganizationsHandler {
  private final OrganizationRepository organizationRepository;

  public Page<Organization> handle(Pageable pageable) {
    return organizationRepository.findAll(pageable);
  }
}
