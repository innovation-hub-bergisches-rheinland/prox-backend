package de.innovationhub.prox.modules.tag.domain.tagcollection.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import java.util.List;
import java.util.UUID;

public record TagCollectionCreated(UUID id, List<Tag> tags) implements Event {

  public static TagCollectionCreated from(TagCollection tagCollection) {
    return new TagCollectionCreated(tagCollection.getId(), tagCollection.getTags());
  }
}
