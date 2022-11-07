package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrganizationDtoAssembler {
  private final TagCollectionFacade tagCollectionFacade;
  private final OrganizationDtoMapper organizationDtoMapper;

  public ReadOrganizationDto toDto(Organization organization) {
    if (organization.getTags() != null) {
      var tagCollectionView = tagCollectionFacade.get(organization.getTags().getTagCollectionId());
      if (tagCollectionView.isPresent()) {
        return organizationDtoMapper.toDto(organization, tagCollectionView.get().tags());
      }
    }

    return organizationDtoMapper.toDto(organization, Collections.emptyList());
  }
}
