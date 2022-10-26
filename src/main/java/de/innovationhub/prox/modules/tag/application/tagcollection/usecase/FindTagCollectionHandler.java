package de.innovationhub.prox.modules.tag.application.tagcollection.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindTagCollectionHandler implements UseCaseHandler<Optional<TagCollection>, FindTagCollection> {
  private final TagCollectionRepository tagCollectionRepository;

  @Override
  public Optional<TagCollection> handle(FindTagCollection useCase) {
    return tagCollectionRepository.findById(useCase.id());
  }
}
