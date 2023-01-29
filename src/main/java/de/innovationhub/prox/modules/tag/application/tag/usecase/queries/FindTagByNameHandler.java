package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindTagByNameHandler {

  private final TagRepository tagRepository;

  public List<Tag> handle(Collection<String> tagNames) {
    Objects.requireNonNull(tagNames);

    if (tagNames.isEmpty()) {
      return List.of();
    }

    return tagRepository.findAllByTagNameInIgnoreCase(tagNames);
  }
}
