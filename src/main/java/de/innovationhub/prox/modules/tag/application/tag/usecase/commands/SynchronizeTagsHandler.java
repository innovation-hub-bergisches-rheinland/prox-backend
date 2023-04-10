package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SynchronizeTagsHandler {
  private final TagRepository tagRepository;

  public List<Tag> handle(Collection<String> tags) {
    Objects.requireNonNull(tags);

    if (tags.isEmpty()) {
      return List.of();
    }

    List<Tag> tagElements = tags.stream().map(Tag::create).toList();
    var tagNames = tagElements.stream().map(Tag::getTagName).toList();
    List<Tag> existingTags = this.tagRepository.findAllByTagNameInIgnoreCase(tagNames);
    List<Tag> createdTags = tagElements.stream()
        .filter(
            tag -> existingTags.stream().noneMatch(t -> t.isEquivalent(tag)))
        .toList();
    if (!createdTags.isEmpty()) {
      this.tagRepository.saveAll(createdTags);
    }

    return Stream.concat(existingTags.stream(), createdTags.stream()).toList();
  }
}
