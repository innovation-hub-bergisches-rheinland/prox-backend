package de.innovationhub.prox.infrastructure.persistence.port;

import de.innovationhub.prox.commons.InfrastructureComponent;
import de.innovationhub.prox.infrastructure.persistence.mapper.TagMapper;
import de.innovationhub.prox.infrastructure.persistence.model.query.QTagEntity;
import de.innovationhub.prox.tag.Tag;
import de.innovationhub.prox.tag.TagPort;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@InfrastructureComponent
public class EBeanTagPort implements TagPort {

  private static final QTagEntity qTag = new QTagEntity();
  private static final TagMapper tagMapper = TagMapper.MAPPER;

  @Override
  public Tag save(Tag tag) {
    var entity = tagMapper.toPersistence(tag);
    entity.save();
    return tagMapper.toDomain(entity);
  }

  @Override
  public Optional<Tag> getByTag(String tag) {
    return qTag
      .tag.eq(tag)
      .findOneOrEmpty()
      .map(tagMapper::toDomain);
  }

  @Override
  public List<Tag> getOrCreateTags(Collection<String> tags) {
    return null;
  }

  @Override
  public boolean existsByTag(String tag) {
    return qTag
      .tag.eq(tag)
      .exists();
  }

  @Override
  public List<Tag> findMatching(String tag) {
    return qTag
      .tag.icontains(tag)
      .findList()
      .stream()
      .map(tagMapper::toDomain)
      .toList();
  }

  @Override
  public List<Tag> findCommon(Collection<String> tags) {
    return null;
  }
}
