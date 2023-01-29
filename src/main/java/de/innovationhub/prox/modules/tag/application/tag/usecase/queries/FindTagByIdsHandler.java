package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindTagByIdsHandler {

  private final TagRepository tagRepository;

  public List<Tag> handle(Collection<UUID> tags) {
    Objects.requireNonNull(tags);

    if (tags.isEmpty()) {
      return List.of();
    }

    return StreamSupport.stream(tagRepository.findAllById(tags).spliterator(), false).toList();
  }
}
