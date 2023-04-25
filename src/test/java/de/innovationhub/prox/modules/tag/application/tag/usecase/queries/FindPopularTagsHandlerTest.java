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
import org.springframework.data.domain.Pageable;

class FindPopularTagsHandlerTest {
  TagCollectionRepository tagCollectionRepository = mock(TagCollectionRepository.class);
  FindPopularTagsHandler handler = new FindPopularTagsHandler(tagCollectionRepository);

  @Test
  void shouldCallRepository() {
    handler.handle(Pageable.unpaged());
    verify(tagCollectionRepository).findPopularTags(Pageable.unpaged());
  }
}