package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@ApplicationComponent
@RequiredArgsConstructor
public class FindTagByIdsHandler {

  private final TagRepository tagRepository;

  public List<Tag> handle(Collection<UUID> tags) {
    return StreamSupport.stream(tagRepository.findAllById(tags).spliterator(), false).toList();
  }
}
