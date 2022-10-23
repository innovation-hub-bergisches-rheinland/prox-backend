package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindMatchingTagsHandler implements UseCaseHandler<List<Tag>, FindMatchingTags> {

  private final TagRepository tagRepository;

  @Override
  public List<Tag> handle(FindMatchingTags useCase) {
    return tagRepository.findMatching(useCase.partialTag());
  }
}
