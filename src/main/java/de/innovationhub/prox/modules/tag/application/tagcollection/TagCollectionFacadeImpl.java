package de.innovationhub.prox.modules.tag.application.tagcollection;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.FindTagCollection;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.FindTagCollectionHandler;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.SetTagCollection;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.SetTagCollectionHandler;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.TagCollectionView;
import de.innovationhub.prox.modules.tag.contract.TagContractViewMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class TagCollectionFacadeImpl implements TagCollectionFacade {
  private final FindTagCollectionHandler findTagCollectionHandler;
  private final SetTagCollectionHandler setTagCollectionHandler;
  private final TagContractViewMapper tagContractViewMapper;

  @Override
  public TagCollectionView get(UUID id) {
    var tc = findTagCollectionHandler.handle(new FindTagCollection(id));
    return tagContractViewMapper.toView(tc);
  }

  @Override
  public TagCollectionView set(UUID id, List<String> tags) {
    var tc = setTagCollectionHandler.handle(new SetTagCollection(id, tags));
    return tagContractViewMapper.toView(tc);
  }
}
