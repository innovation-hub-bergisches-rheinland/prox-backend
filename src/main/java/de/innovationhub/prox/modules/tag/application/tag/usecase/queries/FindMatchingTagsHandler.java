package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.utils.StringUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindMatchingTagsHandler {

  private final TagRepository tagRepository;

  public List<Tag> handle(String partialTag) {
    Objects.requireNonNull(partialTag);

    if (partialTag.isBlank()) {
      return List.of();
    }

    var tag = Tag.create(partialTag);
    return tagRepository.findMatching(tag.getTagName());
  }
}
