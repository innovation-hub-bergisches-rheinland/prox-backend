package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.event.EventPublisher;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.tag.application.tag.events.TagCreated;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagPort;

@ApplicationComponent
public class FindOrCreateTagHandler implements UseCaseHandler<Tag, FindOrCreateTag> {
  private final TagPort tagPort;
  private final EventPublisher eventPublisher;

  public FindOrCreateTagHandler(TagPort tagPort, EventPublisher eventPublisher) {
    this.tagPort = tagPort;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public Tag handle(FindOrCreateTag useCase) {
    var foundTag = tagPort.getByTag(useCase.tag());
    if (foundTag.isPresent()) {
      return foundTag.get();
    }

    var tag = new Tag(useCase.tag());
    tagPort.save(tag);
    this.eventPublisher.publish(TagCreated.from(tag));
    return tag;
  }
}
