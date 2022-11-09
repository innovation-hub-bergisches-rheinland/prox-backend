package de.innovationhub.prox.modules.tag.domain.tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import java.util.UUID;
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
}
