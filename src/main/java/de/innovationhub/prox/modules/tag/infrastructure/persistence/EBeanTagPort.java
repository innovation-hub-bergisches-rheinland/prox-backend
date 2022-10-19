package de.innovationhub.prox.modules.tag.infrastructure.persistence;

import de.innovationhub.prox.modules.commons.infrastructure.InfrastructureComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagPort;
import de.innovationhub.prox.modules.tag.infrastructure.persistence.mapper.TagMapper;
import de.innovationhub.prox.modules.tag.infrastructure.persistence.model.query.QTagEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    return qTag.tag.eq(tag).findOneOrEmpty().map(tagMapper::toDomain);
  }

  @Override
  public List<Tag> getByIdIn(Collection<UUID> ids) {
    return qTag.id.in(ids).findStream().map(tagMapper::toDomain).toList();
  }

  @Override
  public List<Tag> getOrCreateTags(Collection<String> tags) {
    return null;
  }

  @Override
  public boolean existsByTag(String tag) {
    return qTag.tag.eq(tag).exists();
  }

  @Override
  public List<Tag> findMatching(String tag) {
    return qTag.tag.icontains(tag).findList().stream().map(tagMapper::toDomain).toList();
  }

  @Override
  public List<Tag> findCommon(Collection<String> tags) {
    return null;
  }
}
