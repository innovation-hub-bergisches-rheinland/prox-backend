package de.innovationhub.prox.modules.tag.application.tagcollection.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class SplitTagsHandler {
  private final TagRepository tagRepository;
  private final TagCollectionRepository tagCollectionRepository;
  private final TagDtoMapper tagDtoMapper;

  @Transactional
  @PreAuthorize("hasRole('admin')")
  public List<TagDto> handle(UUID tagIdToSplit, List<UUID> splittedTags) {
    var tagToSplit = tagRepository.findById(tagIdToSplit).orElseThrow();
    var tags = StreamSupport.stream(tagRepository.findAllById(splittedTags).spliterator(), false).collect(
        Collectors.toSet());

    var relevantTagCollections = tagCollectionRepository.findWithAnyTag(List.of(tagIdToSplit));
    for (TagCollection tagCollection : relevantTagCollections) {
      tagCollection.splitTag(tagToSplit, tags);
    }
    tagCollectionRepository.saveAll(relevantTagCollections);
    return tagDtoMapper.toDtoList(new ArrayList<>(tags));
  }
}
