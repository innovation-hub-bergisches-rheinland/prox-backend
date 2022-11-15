package de.innovationhub.prox.modules.tag.application.tag.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;

class FindCommonTagsHandlerTest {
  TagCollectionRepository tagCollectionRepository = mock(TagCollectionRepository.class);
  FindCommonTagsHandler handler = new FindCommonTagsHandler(tagCollectionRepository);

  @Test
  void shouldThrowWhenTagsIsNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null, 0));
  }

  @Test
  void shouldThrowWhenTagsIsEmpty() {
    List<String> list = List.of();
    var limit = 2;

    assertThrows(IllegalArgumentException.class, () -> handler.handle(list, limit));
  }

  @Test
  void shouldThrowWhenLimitIsZero() {
    var list = List.of("test");
    var limit = 0;

    assertThrows(IllegalArgumentException.class, () -> handler.handle(list, limit));
  }

  @Test
  void shouldCallRepository() {
    var list = List.of("test1", "test2");
    var limit = 2;

    handler.handle(list, limit);
    var captor = ArgumentCaptor.forClass(PageRequest.class);
    verify(tagCollectionRepository).findCommonUsedTagsWith(eq(list), captor.capture());
    var pageRequest = captor.getValue();

    assertThat(pageRequest.getPageNumber()).isZero();
    assertThat(pageRequest.getPageSize()).isEqualTo(limit);
  }
}