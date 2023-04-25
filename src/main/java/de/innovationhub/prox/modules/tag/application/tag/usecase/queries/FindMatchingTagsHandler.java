package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.utils.StringUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindMatchingTagsHandler {

  private final TagRepository tagRepository;

  public Page<Tag> handle(String partialTag, Pageable pageable) {
    Objects.requireNonNull(partialTag);

    if (partialTag.isBlank()) {
      return Page.empty();
    }

    var tag = Tag.create(partialTag);
    return tagRepository.findMatching(tag.getTagName(), pageable);
  }
}
