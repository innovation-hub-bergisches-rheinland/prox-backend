package de.innovationhub.prox.modules.organization.application.usecase.commands;

import de.innovationhub.prox.commons.exception.UnauthorizedAccessException;
import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.application.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.List;
import java.util.UUID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class SetOrganizationTagsHandler {

  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;
  private final TagCollectionFacade tagCollectionFacade;

  @Transactional
  @PreAuthorize("@organizationPermissionEvaluator.hasPermission(#organizationId, authentication)")
  public List<TagDto> handle(UUID organizationId,
      List<UUID> tags) {
    var organization = organizationRepository.findById(organizationId)
        .orElseThrow(OrganizationNotFoundException::new);
    var authenticatedUser = authenticationFacade.currentAuthenticatedId();

    if (!organization.isInRole(authenticatedUser, OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    var tagCollection = tagCollectionFacade.setTagCollection(organization.getTagCollectionId(), tags);
    organization.setTagCollectionId(tagCollection.id());
    organizationRepository.save(organization);
    return tagCollection.tags();
  }
}
