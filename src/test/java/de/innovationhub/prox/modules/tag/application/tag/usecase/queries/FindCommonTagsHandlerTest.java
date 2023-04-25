package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindCommonTagsHandler;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class FindCommonTagsHandlerTest {
  TagCollectionRepository tagCollectionRepository = mock(TagCollectionRepository.class);
  FindCommonTagsHandler handler = new FindCommonTagsHandler(tagCollectionRepository);

  @Test
  void shouldThrowWhenTagsIsNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null, Pageable.unpaged()));
  }

  @Test
  void shouldReturnEmptyListWhenTagsIsEmpty() {
    List<String> list = List.of();

    assertThat(handler.handle(list, Pageable.unpaged()))
        .isEmpty();
  }

  @Test
  void shouldCallRepository() {
    var list = List.of("test1", "test2");

    handler.handle(list, Pageable.unpaged());
    verify(tagCollectionRepository).findCommonUsedTagsWith(eq(list), eq(Pageable.unpaged()));
  }
}