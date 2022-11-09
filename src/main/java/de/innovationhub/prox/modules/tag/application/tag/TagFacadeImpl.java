package de.innovationhub.prox.modules.tag.application.tag;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.usecase.FindTagByIdsHandler;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class TagFacadeImpl implements TagFacade {
  private final FindTagByIdsHandler findTagByIdsHandler;

  @Override
  public List<String> getTags(Collection<UUID> id) {
    return findTagByIdsHandler.handle(id).stream().map(Tag::getTagName).toList();
  }
}
