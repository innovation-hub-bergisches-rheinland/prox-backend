package de.innovationhub.prox.modules.tag.domain.tag.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.UUID;

public record TagCreated(UUID id, String tag) implements Event {
  public static TagCreated from(Tag tag) {
    return new TagCreated(tag.getId(), tag.getTag());
  }
}
