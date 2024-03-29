package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class FindTagByNameHandlerTest {
  TagRepository tagRepository = mock(TagRepository.class);
  FindTagByNameHandler handler = new FindTagByNameHandler(tagRepository);

  @Test
  void shouldThrowWhenTagsIsNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null));
  }

  @Test
  void shouldReturnEmptyList() {
    var result = handler.handle(List.of());

    assertThat(result).isEmpty();
  }

  @Test
  void shouldCallRepository() {
    var tags = List.of("tag1", "tag2");

    handler.handle(tags);

    verify(tagRepository).findAllByTagNameInIgnoreCase(tags);
  }
}