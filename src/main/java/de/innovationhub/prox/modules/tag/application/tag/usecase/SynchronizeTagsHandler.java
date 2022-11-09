package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SynchronizeTagsHandler {
  private final TagRepository tagRepository;

  public List<Tag> handle(Collection<String> tags) {
    return tagRepository.fetchOrCreateTags(tags);
  }
}
