package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindMatchingTagsHandler;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Pageable;

class FindMatchingTagsHandlerTest {
  TagRepository tagRepository = mock(TagRepository.class);
  FindMatchingTagsHandler handler = new FindMatchingTagsHandler(tagRepository);

  @Test
  void shouldThrowOnNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null, Pageable.unpaged()));
  }

  @ParameterizedTest
  @ValueSource(strings = { "", " ", "  ", "\t\n" })
  void shouldReturnEmptyListOnBlankString(String blankString) {
    var result = handler.handle(blankString, Pageable.unpaged());

    assertThat(result).isEmpty();
  }

  @Test
  void shouldCallRepository() {
    var partialTag = "test";
    handler.handle(partialTag, Pageable.unpaged());

    verify(tagRepository).findMatching(partialTag, Pageable.unpaged());
  }
}