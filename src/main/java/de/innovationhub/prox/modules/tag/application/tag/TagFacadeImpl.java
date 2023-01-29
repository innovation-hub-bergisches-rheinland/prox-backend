package de.innovationhub.prox.modules.tag.application.tag;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindTagByIdsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindTagByNameHandler;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.TagView;
import de.innovationhub.prox.modules.tag.contract.TagViewMapper;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class TagFacadeImpl implements TagFacade {

  private final FindTagByIdsHandler findTagByIdsHandler;
  private final FindTagByNameHandler findTagByNameHandler;
  private final TagViewMapper tagViewMapper;

  @Override
  @Cacheable(CacheConfig.TAGS)
  public List<String> getTagsAsString(Collection<UUID> id) {
    return findTagByIdsHandler.handle(id).stream().map(Tag::getTagName).toList();
  }

  @Override
  public List<TagView> getTags(Collection<UUID> id) {
    return findTagByIdsHandler.handle(id).stream().map(tagViewMapper::toView).toList();
  }

  @Override
  @Cacheable(CacheConfig.TAGS)
  public List<TagView> getTagsByName(Collection<String> id) {
    return findTagByNameHandler.handle(id).stream().map(tagViewMapper::toView).toList();
  }
}
