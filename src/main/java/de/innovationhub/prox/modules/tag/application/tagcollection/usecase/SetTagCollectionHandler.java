package de.innovationhub.prox.modules.tag.application.tagcollection.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.TagDomainService;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetTagCollectionHandler {

  private final TagDomainService tagDomainService;

  public TagCollection handle(UUID id, List<String> tags) {
    return tagDomainService.updateCollection(id, tags);
  }
}
