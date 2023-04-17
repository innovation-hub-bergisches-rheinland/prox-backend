package de.innovationhub.prox.modules.organization.application.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class SearchOrganizationHandler {

  private final OrganizationRepository organizationRepository;
  private final TagFacade tagFacade;
  private final TagCollectionFacade tagCollectionFacade;

  public Page<Organization> handle(
      String query,
      Collection<String> tags,
      Pageable pageable) {
    List<UUID> tagCollectionIds = new ArrayList<>();
    if(tags != null) {
      var tagIds = tagFacade.getTagsByName(tags).stream().map(TagDto::id).toList();
      tagCollectionIds.addAll(
          tagCollectionFacade.findWithAllTags(tagIds).stream().map(TagCollectionDto::id).toList());
    }

    return organizationRepository.search(query, tagCollectionIds, pageable);
  }
}
