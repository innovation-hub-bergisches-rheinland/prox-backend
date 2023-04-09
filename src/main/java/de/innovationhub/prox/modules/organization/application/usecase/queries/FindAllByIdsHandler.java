package de.innovationhub.prox.modules.organization.application.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllByIdsHandler {
  private final OrganizationRepository organizationRepository;

  public List<Organization> handle(Collection<UUID> ids) {
    return organizationRepository.findAllById (ids);
  }
}
