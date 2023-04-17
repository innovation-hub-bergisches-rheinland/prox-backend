package de.innovationhub.prox.modules.tag.application.tagcollection;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.queries.FindWithAnyTagHandler;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.queries.GetTagCollectionHandler;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.SetTagCollectionHandler;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class TagCollectionFacadeImpl implements TagCollectionFacade {
  private final GetTagCollectionHandler getTagCollectionHandler;
  private final SetTagCollectionHandler setTagCollectionHandler;
  private final FindWithAnyTagHandler findWithAnyTagHandler;

  @Override
  public Optional<TagCollectionDto> getTagCollection(UUID id) {
    return getTagCollectionHandler.handle(id);
  }

  @Override
  public TagCollectionDto setTagCollection(UUID id, Collection<UUID> tags) {
    return setTagCollectionHandler.handle(id, tags);
  }

  @Override
  public List<TagCollectionDto> findWithAnyTag(Collection<UUID> tags) {
    return findWithAnyTagHandler.handle(tags);
  }
}
