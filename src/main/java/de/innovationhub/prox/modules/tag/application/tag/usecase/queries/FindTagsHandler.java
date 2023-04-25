package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import jakarta.annotation.Nullable;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindTagsHandler {

  private final TagRepository tagRepository;

  public Page<Tag> handle(@Nullable String partialTag, Pageable pageable) {
    if (partialTag == null) {
      return tagRepository.findAll(pageable);
    }

    if (partialTag.isBlank()) {
      return Page.empty();
    }

    var tag = Tag.create(partialTag);
    return tagRepository.findMatching(tag.getTagName(), pageable);
  }
}
