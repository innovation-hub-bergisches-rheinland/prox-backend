package de.innovationhub.prox.modules.organization.application.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.TagView;
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

  public Page<Organization> handle(
      String query,
      Collection<String> tags,
      Pageable pageable) {
    List<UUID> tagIds = new ArrayList<>();
    if(tags != null) {
      tagIds.addAll(
          tagFacade.getTagsByName(tags).stream().map(TagView::id).toList()
      );
    }

    return organizationRepository.search(query, tagIds, pageable);
  }
}