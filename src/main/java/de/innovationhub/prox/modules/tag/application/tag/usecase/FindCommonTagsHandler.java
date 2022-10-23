package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
@ApplicationComponent
public class FindCommonTagsHandler implements UseCaseHandler<List<Tag>, FindCommonTags> {
  private final TagCollectionRepository tagCollectionRepository;

  @Override
  public List<Tag> handle(FindCommonTags useCase) {
    var pageRequest = PageRequest.of(0, useCase.limit());
    return tagCollectionRepository.findCommonUsedTagsWith(useCase.tags(), pageRequest);
  }
}
