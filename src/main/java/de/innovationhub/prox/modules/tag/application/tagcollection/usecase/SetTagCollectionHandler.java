package de.innovationhub.prox.modules.tag.application.tagcollection.usecase;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tagcollection.dto.TagCollectionDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetTagCollectionHandler {
  private final TagCollectionRepository tagCollectionRepository;
  private final TagRepository tagRepository;
  private final TagCollectionDtoMapper dtoMapper;

  public TagCollectionDto handle(UUID id, Collection<UUID> tags) {
    var tc = tagCollectionRepository.findById(id)
        .orElse(TagCollection.create(id));
    var setTags = StreamSupport.stream(tagRepository.findAllById(tags).spliterator(), false)
        .toList();
    tc.setTags(setTags);
    return dtoMapper.toDto(tagCollectionRepository.save(tc));
  }
}
