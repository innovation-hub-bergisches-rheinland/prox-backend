package de.innovationhub.prox.modules.tag.domain.tagcollection.events;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import java.util.List;
import java.util.UUID;

public record TagCollectionUpdated(UUID id, List<Tag> tags) implements Event {

  public static TagCollectionUpdated from(TagCollection tagCollection) {
    return new TagCollectionUpdated(tagCollection.getId(), tagCollection.getTags());
  }
}
