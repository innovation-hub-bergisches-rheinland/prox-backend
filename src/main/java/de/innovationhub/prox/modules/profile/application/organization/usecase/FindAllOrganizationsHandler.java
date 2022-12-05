package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
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
