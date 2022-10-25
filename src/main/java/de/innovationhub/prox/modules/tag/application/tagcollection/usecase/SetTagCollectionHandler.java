package de.innovationhub.prox.modules.tag.application.tagcollection.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.domain.TagDomainService;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetTagCollectionHandler implements UseCaseHandler<TagCollection, SetTagCollection> {
  private final TagDomainService tagDomainService;

  @Override
  public TagCollection handle(SetTagCollection useCase) {
    return tagDomainService.updateCollection(useCase.id(), useCase.tags());
  }
}
