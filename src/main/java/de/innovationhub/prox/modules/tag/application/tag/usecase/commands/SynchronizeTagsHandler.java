package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.utils.StringUtils;
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

    List<Tag> tagElements = tags.stream().map(Tag::create).toList();
    List<Tag> existingTags = this.tagRepository.findAllByTagNameInIgnoreCase(tags);
    List<Tag> notExistingTags = tagElements.stream()
        .filter(
            tag -> existingTags.stream().noneMatch(t -> t.isEquivalent(tag)))
        .toList();
    List<Tag> createdTags = new ArrayList<>(notExistingTags);
    if (!createdTags.isEmpty()) {
      this.tagRepository.saveAll(createdTags);
    }

    return Stream.concat(existingTags.stream(), createdTags.stream()).toList();
  }
}
