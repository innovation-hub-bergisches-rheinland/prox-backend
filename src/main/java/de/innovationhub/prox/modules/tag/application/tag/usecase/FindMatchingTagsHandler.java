package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagPort;
import java.util.List;

@ApplicationComponent
public class FindMatchingTagsHandler implements UseCaseHandler<List<Tag>, FindMatchingTags> {
  private final TagPort tagPort;

  public FindMatchingTagsHandler(TagPort tagPort) {
    this.tagPort = tagPort;
  }

  @Override
  public List<Tag> handle(FindMatchingTags useCase) {
    return tagPort.findMatching(useCase.partialTag());
  }
}
