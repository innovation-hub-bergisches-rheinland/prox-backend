package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class SynchronizeTagsHandlerTest {
  TagRepository tagRepository = mock(TagRepository.class);
  SynchronizeTagsHandler handler = new SynchronizeTagsHandler(tagRepository);

  @Test
  void shouldThrowOnNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null));
  }

  @Test
  void shouldReturnEmptyList() {
    var result = handler.handle(List.of());

    assertThat(result).isEmpty();
  }

  @Test
  void shouldCreateMissingTag() {
    var tagInput = List.of("test1", "test2");
    var returnedTags = handler.handle(tagInput);

    assertThat(returnedTags)
        .extracting(Tag::getTagName)
        .containsExactlyElementsOf(tagInput);
    verify(tagRepository).saveAll(returnedTags);
  }

  @Test
  void shouldReturnTags() {
    var givenTags = List.of(Tag.create("test3"), Tag.create("test4"));
    when(tagRepository.findAllByTagNameInIgnoreCase(any())).thenReturn(givenTags);

    var tagInput = List.of("test3", "test4");
    var returnedTags = handler.handle(tagInput);

    assertThat(returnedTags)
        .extracting(Tag::getTagName)
        .containsExactlyElementsOf(tagInput);
    verify(tagRepository, times(0)).saveAll(any());
  }

  @Test
  void shouldReturnSluggedTags() {
    var givenTags = List.of(Tag.create("test-3"), Tag.create("test-4"));
    when(tagRepository.findAllByTagNameInIgnoreCase(any())).thenReturn(givenTags);

    var tagInput = List.of("test 3", "test-4");
    var returnedTags = handler.handle(tagInput);

    assertThat(returnedTags)
        .containsExactlyElementsOf(givenTags);
    verify(tagRepository, times(0)).saveAll(any());
  }
}