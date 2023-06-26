package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
  public List<TagDto> handle(UUID tagIdToSplit, List<String> splittedTags) {
    var tagToSplit = tagRepository.findById(tagIdToSplit).orElseThrow();
    var existingSplittedTags = tagRepository.findAllByTagNameInIgnoreCase(splittedTags);
    var newlyTags = splittedTags.stream()
        .filter(t -> existingSplittedTags.stream().noneMatch(t1 -> t1.getTagName().equalsIgnoreCase(t)))
        .map(Tag::create)
        .toList();
    var allTags = Stream.concat(existingSplittedTags.stream(), newlyTags.stream()).toList();

    tagRepository.saveAll(newlyTags);

    var relevantTagCollections = tagCollectionRepository.findWithAnyTag(List.of(tagIdToSplit));
    for (TagCollection tagCollection : relevantTagCollections) {
      var tags = tagCollection.getTags()
          .stream()
          .filter(t -> !t.getId().equals(tagIdToSplit))
          .toList();
      var newTags = mergeTagLists(tags, allTags);
      tagCollection.setTags(newTags);
    }
    tagCollectionRepository.saveAll(relevantTagCollections);
    tagCollectionRepository.deleteAllTags(tagToSplit.getId());
    tagRepository.delete(tagToSplit);
    return tagDtoMapper.toDtoList(allTags);
  }

  private List<Tag> mergeTagLists(List<Tag> tagList1, List<Tag> tagList2) {
    return Stream.concat(tagList1.stream(), tagList2.stream())
        .collect(Collectors.toSet())
        .stream()
        .toList();
  }
}
