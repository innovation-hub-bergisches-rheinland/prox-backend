package de.innovationhub.prox.modules.tag.domain.tag.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.UUID;

public record TagCreated(UUID id, String tagName) implements DomainEvent {

  public static TagCreated from(Tag tag) {
    return new TagCreated(tag.getId(), tag.getTagName());
  }
}
