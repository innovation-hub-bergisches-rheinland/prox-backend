package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindPopularTagsHandler;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;

class FindPopularTagsHandlerTest {
  TagCollectionRepository tagCollectionRepository = mock(TagCollectionRepository.class);
  FindPopularTagsHandler handler = new FindPopularTagsHandler(tagCollectionRepository);

  @Test
  void shouldThrowWhenLimitIsZero() {
    var limit = 0;

    assertThrows(IllegalArgumentException.class, () -> handler.handle(limit));
  }

  @Test
  void shouldCallRepository() {
    var limit = 2;

    handler.handle(limit);
    var captor = ArgumentCaptor.forClass(PageRequest.class);
    verify(tagCollectionRepository).findPopularTags(captor.capture());
    var pageRequest = captor.getValue();

    assertThat(pageRequest.getPageNumber()).isZero();
    assertThat(pageRequest.getPageSize()).isEqualTo(limit);
  }
}