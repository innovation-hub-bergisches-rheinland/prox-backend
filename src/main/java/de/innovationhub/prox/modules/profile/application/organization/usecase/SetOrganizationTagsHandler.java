package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetOrganizationTagsHandler {
  private final OrganizationRepository organizationRepository;

  public List<UUID> handle(UUID organizationId,
      List<UUID> tags) {
    var organization = organizationRepository.findById(organizationId)
        .orElseThrow(() -> new RuntimeException("Organization not found")); // TODO: Create custom exception
    organization.setTags(tags);
    organizationRepository.save(organization);
    return List.copyOf(organization.getTags());
  }
}
