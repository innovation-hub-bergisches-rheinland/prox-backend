package de.innovationhub.prox.modules.organization.application.usecase.commands;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.organization.application.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.user.application.account.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.user.contract.account.AuthenticationFacade;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class SetOrganizationLogoHandler {
  private static final String LOGO_FILE_PREFIX = "organization-logo-";

  private final StorageProvider storage;
  private final AuthenticationFacade authentication;
  private final OrganizationRepository organizationRepository;

  @PreAuthorize("@organizationPermissionEvaluator.hasPermission(#orgId, authentication)")
  public void handle(UUID orgId, byte[] avatarImageData, String contentType) {
    var org = organizationRepository.findById(orgId)
        .orElseThrow(OrganizationNotFoundException::new);
    if(!org.isInRole(authentication.currentAuthenticatedId(), OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    var fileId = LOGO_FILE_PREFIX + orgId;
    try {
      storage.storeFile(fileId, avatarImageData, contentType);
      org.setLogoKey(fileId);
      organizationRepository.save(org);
    } catch (IOException e) {
      throw new RuntimeException("Failed to store avatar image", e);
    }
  }
}
