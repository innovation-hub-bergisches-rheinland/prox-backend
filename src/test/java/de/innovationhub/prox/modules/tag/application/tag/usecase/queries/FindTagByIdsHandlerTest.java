package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindTagByIdsHandler;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FindTagByIdsHandlerTest {
  TagRepository tagRepository = mock(TagRepository.class);
  FindTagByIdsHandler handler = new FindTagByIdsHandler(tagRepository);

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
    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());

    handler.handle(tags);

    verify(tagRepository).findAllById(tags);
  }
}