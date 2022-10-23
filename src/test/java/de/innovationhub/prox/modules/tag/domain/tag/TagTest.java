package de.innovationhub.prox.modules.tag.domain.tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
