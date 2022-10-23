package de.innovationhub.prox.modules.tag.domain;

import de.innovationhub.prox.modules.commons.domain.DomainComponent;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Collection;
import java.util.UUID;
import javax.transaction.Transactional;

// The Tag Domain Service is necessary because it needs to coordinate changes between Tags
// and a Tag collection
@DomainComponent
public class TagDomainService {
  private final TagRepository tagRepository;
  private final TagCollectionRepository tagCollectionRepository;

  public TagDomainService(TagRepository tagRepository,
      TagCollectionRepository tagCollectionRepository) {
    this.tagRepository = tagRepository;
    this.tagCollectionRepository = tagCollectionRepository;
  }

  @Transactional
  public TagCollection createCollection(Collection<String> tags) {
    var tagEntities = tagRepository.fetchOrCreateTags(tags);

    var tagCollection = TagCollection.create(UUID.randomUUID(), tagEntities);
    tagCollection = tagCollectionRepository.save(tagCollection);
    return tagCollection;
  }

  @Transactional
  public TagCollection updateCollection(UUID collection, Collection<String> tags) {
    var tagCollection = tagCollectionRepository.findById(collection)
        .orElseThrow();
    var tagEntities = tagRepository.fetchOrCreateTags(tags);

    tagCollection.setTags(tagEntities);

    tagCollection = tagCollectionRepository.save(tagCollection);
    return tagCollection;
  }
}
