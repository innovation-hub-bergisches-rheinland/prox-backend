package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.ArrayList;
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

    List<Tag> existingTags = this.tagRepository.findAllByTagNameInIgnoreCase(tags);
    List<String> notExistingTags = tags.stream()
        .filter(
            strTag -> existingTags.stream().noneMatch(t -> t.getTagName().equalsIgnoreCase(strTag)))
        .toList();
    List<Tag> createdTags = new ArrayList<>();
    for (var tag : notExistingTags) {
      createdTags.add(Tag.create(tag));
    }
    if (!createdTags.isEmpty()) {
      this.tagRepository.saveAll(createdTags);
    }

    return Stream.concat(existingTags.stream(), createdTags.stream()).toList();
  }
}
