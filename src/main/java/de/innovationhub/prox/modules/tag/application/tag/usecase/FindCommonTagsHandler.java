package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
@ApplicationComponent
public class FindCommonTagsHandler {

  private final TagCollectionRepository tagCollectionRepository;

  public List<Tag> handle(Collection<String> tags, int limit) {
    Objects.requireNonNull(tags);

    if (tags.isEmpty()) {
      throw new IllegalArgumentException("Must provide at least one tag");
    }
    if (limit <= 0) {
      throw new IllegalArgumentException("Limit must be greater than 0");
    }

    var pageRequest = PageRequest.of(0, limit);
    return tagCollectionRepository.findCommonUsedTagsWith(tags, pageRequest);
  }
}
