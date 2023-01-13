package de.innovationhub.prox.modules.tag.domain.tagcollection.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import java.util.UUID;

public record TagCollectionCreated(UUID id) implements DomainEvent {

  public static TagCollectionCreated from(TagCollection tagCollection) {
    return new TagCollectionCreated(tagCollection.getId());
  }
}
