package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindAllOrganizationsHandler {
  private final OrganizationRepository organizationRepository;

  public List<Organization> handle() {
    return StreamSupport.stream(organizationRepository.findAll().spliterator(), false).toList();
  }
}
