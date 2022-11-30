package de.innovationhub.prox.modules.profile.application.organization;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationPermissionEvaluator {
  private final OrganizationRepository organizationRepository;

  public boolean hasPermission(Organization target, Authentication authentication) {
    if(authentication == null || !authentication.isAuthenticated()) return false;

    var id = UUID.fromString(authentication.getName());
    return target.isInRole(id, OrganizationRole.ADMIN);
  }

  public boolean hasPermission(UUID organizationId, Authentication authentication) {
    // To prevent querying the database
    if(authentication == null || !authentication.isAuthenticated()) return false;

    var optOrganization = organizationRepository.findById(organizationId);
    // It's better to deny than throwing an exception here
    if(optOrganization.isEmpty()) return false;

    return hasPermission(optOrganization.get(), authentication);
  }
}
