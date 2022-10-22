package de.innovationhub.prox.modules.tag.domain;

import de.innovationhub.prox.modules.commons.domain.DomainComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
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
    var tagEntities = fetchOrCreateTags(tags);
    var tagEntitiesIds = tagEntities
        .stream().map(Tag::getId)
        .toList();

    var tagCollection = TagCollection.create(UUID.randomUUID(), tagEntitiesIds);
    tagCollection = tagCollectionRepository.save(tagCollection);
    return tagCollection;
  }

  @Transactional
  public TagCollection updateCollection(UUID collection, Collection<String> tags) {
    var tagCollection = tagCollectionRepository.findById(collection)
        .orElseThrow();
    var tagEntities = fetchOrCreateTags(tags);
    var tagEntitiesIds = tagEntities
        .stream().map(Tag::getId)
        .toList();
    tagCollection.setTags(tagEntitiesIds);

    tagCollection = tagCollectionRepository.save(tagCollection);
    return tagCollection;
  }

  private List<Tag> fetchOrCreateTags(Collection<String> tags) {
    List<Tag> existingTags = tagRepository.findAllByTagIn(tags);
    List<String> notExistingTags = tags.stream()
        .filter(strTag -> existingTags.stream().noneMatch(t -> t.getTag().equalsIgnoreCase(strTag)))
        .toList();
    List<Tag> createdTags = new ArrayList<>();
    for (var tag : notExistingTags) {
      createdTags.add(Tag.createNew(tag));
    }
    if(!createdTags.isEmpty()) {
      tagRepository.saveAll(createdTags);
    }

    return Stream.concat(existingTags.stream(), createdTags.stream()).toList();
  }
}
