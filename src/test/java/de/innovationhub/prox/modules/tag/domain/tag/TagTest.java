package de.innovationhub.prox.modules.tag.domain.tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import java.util.Set;
import java.util.UUID;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagMerged;
import org.junit.jupiter.api.Test;

class TagTest {

  @Test
  void shouldThrowWhenTagIsBlank() {
    assertThrows(RuntimeException.class, () -> new Tag(UUID.randomUUID(), "   "));
  }

  @Test
  void shouldConvertTagToLowercase() {
    var tag = new Tag(UUID.randomUUID(), "TEST");
    assertThat(tag.getTagName()).isEqualTo("test");
  }

  @Test
  void shouldRegisterTagCreatedEventOnCreate() {
    var tag = Tag.create("test");
    var domainEvents = tag.getDomainEvents();

    assertThat(domainEvents)
        .hasSize(1)
        .first()
        .isInstanceOf(TagCreated.class);
  }

  @Test
  void shouldRegisterTagMergedEventOnMerge() {
    var tag = Tag.create("test");
    var tagToMerge = Tag.create("test 2");
    tag.merge(tagToMerge);
    var domainEvents = tag.getDomainEvents();

    assertThat(domainEvents)
        .filteredOn(event -> event instanceof TagMerged)
        .hasSize(1)
        .first();
  }

  @Test
  void shouldThrowOnEqualTagMerge() {
    var tag = Tag.create("test");
    assertThrows(IllegalArgumentException.class, () -> tag.merge(tag));
  }

  @Test
  void shouldAddNameAsAliasOnMerge() {
    var tag = Tag.create("test");
    var tag1 = Tag.create("test1");
    tag.merge(tag1);
    assertThat(tag.getAliases())
        .containsExactly("test1");
  }

  @Test
  void shouldAddAliasesOnMerge() {
    var tag = Tag.create("test");
    var tag1 = Tag.create("test1");
    tag.merge(tag1);

    var tag2 = Tag.create("test2");
    tag.merge(tag2);

    assertThat(tag.getAliases())
        .containsExactlyInAnyOrder("test1", "test2");
  }

  @Test
  void shouldUpdateAliases() {
    var tag = Tag.create("test");
    var aliases = Set.of("test1", "test2");

    tag.updateAliases(aliases);

    assertThat(tag.getAliases())
        .containsExactlyInAnyOrderElementsOf(aliases);
  }
}
