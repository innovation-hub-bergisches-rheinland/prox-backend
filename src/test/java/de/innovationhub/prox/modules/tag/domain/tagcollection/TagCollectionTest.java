package de.innovationhub.prox.modules.tag.domain.tagcollection;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionCreated;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionUpdated;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TagCollectionTest {

  @Test
  void shouldRegisterTagCollectionCreatedEventOnCreate() {
    var tagCollection = TagCollection.create(UUID.randomUUID());
    var domainEvents = tagCollection.getDomainEvents();

    assertThat(domainEvents)
        .hasSize(1)
        .first()
        .isInstanceOf(TagCollectionCreated.class);
  }

  @Test
  void shouldRegisterTagCollectionUpdatedEventOnCreate() {
    var tagCollection = new TagCollection(UUID.randomUUID(), new ArrayList<>());
    tagCollection.setTags(new ArrayList<>());
    var domainEvents = tagCollection.getDomainEvents();

    assertThat(domainEvents)
        .hasSize(1)
        .first()
        .isInstanceOf(TagCollectionUpdated.class);
  }
}