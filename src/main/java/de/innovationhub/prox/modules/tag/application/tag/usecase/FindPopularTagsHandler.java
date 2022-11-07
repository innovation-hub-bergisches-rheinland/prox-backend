package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@ApplicationComponent
@RequiredArgsConstructor
public class FindPopularTagsHandler {

  private final TagCollectionRepository tagCollectionRepository;

  public List<Tag> handle(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("Limit must be greater than 0");
    }

    var pageRequest = PageRequest.of(0, limit);
    return tagCollectionRepository.findPopularTags(pageRequest);
  }
}
