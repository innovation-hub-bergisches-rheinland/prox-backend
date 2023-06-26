package de.innovationhub.prox.modules.tag.domain.tagcollection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionCreated;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionUpdated;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagSplitted;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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

  @Test
  void shouldThrowWhenNotEnoughTags() {
    var tags = new ArrayList<>(createTags("test1", "test2"));
    var tag = tags.get(0);
    var tagCollection = new TagCollection(UUID.randomUUID(), tags);
    assertThrows(RuntimeException.class, () -> tagCollection.splitTag(tag, createTags("test3")));
    assertThrows(RuntimeException.class, () -> tagCollection.splitTag(tag, createTags()));
  }

  @Test
  void shouldThrowWhenTagIsNotInList() {
    var tags = new ArrayList<>(createTags("test1", "test2"));
    var tag = Tag.create("test3");
    var tagCollection = new TagCollection(UUID.randomUUID(), tags);
    assertThrows(RuntimeException.class, () -> tagCollection.splitTag(tag, createTags("test3", "test4")));
  }

  @Test
  void shouldRegisterTagSplittedEvent() {
    var tags = new ArrayList<>(createTags("test1", "test2"));
    var tag = tags.get(0);
    var tagCollection = new TagCollection(UUID.randomUUID(), tags);
    tagCollection.splitTag(tag, createTags("test3", "test4"));

    var domainEvents = tagCollection.getDomainEvents();

    assertThat(domainEvents)
        .hasSize(1)
        .first()
        .isInstanceOf(TagSplitted.class);
  }

  @Test
  void shouldSplitTags() {
    var tags = new ArrayList<>(createTags("test1", "test2"));
    var tag = tags.get(0);
    var tagCollection = new TagCollection(UUID.randomUUID(), tags);
    var splittedTags = createTags("test3", "test4");
    tagCollection.splitTag(tag, splittedTags);

    assertThat(tagCollection.getTags())
        .doesNotContain(tag);
    assertThat(tagCollection.getTags())
        .containsAll(splittedTags);
  }

  private Set<Tag> createTags(String... tagNames) {
    return Arrays.stream(tagNames)
        .map(Tag::create)
        .collect(Collectors.toSet());
  }
}