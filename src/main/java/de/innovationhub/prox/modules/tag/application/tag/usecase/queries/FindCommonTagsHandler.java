package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import de.innovationhub.prox.utils.StringUtils;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@ApplicationComponent
public class FindCommonTagsHandler {

  private final TagCollectionRepository tagCollectionRepository;

  public Page<Tag> handle(Collection<String> tags, Pageable pageable) {
    Objects.requireNonNull(tags);

    if (tags.isEmpty()) {
      return Page.empty();
    }

    var tagNames = tags.stream().map(Tag::create).map(Tag::getTagName).toList();
    return tagCollectionRepository.findCommonUsedTagsWith(tagNames, pageable);
  }
}
