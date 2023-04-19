package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MergeTagsHandlerTest {
  TagRepository tagRepository = mock(TagRepository.class);
  TagDtoMapper tagDtoMapper = TagDtoMapper.INSTANCE;
  MergeTagsHandler handler = new MergeTagsHandler(tagRepository, tagDtoMapper);


  @Test
  void shouldDeleteMergedTag() {
    var tag = Tag.create("test");
    var tagToMerge = Tag.create("test 2");
    when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
    when(tagRepository.findById(tagToMerge.getId())).thenReturn(Optional.of(tagToMerge));

    handler.handle(tagToMerge.getId(), tag.getId());

    verify(tagRepository).delete(tagToMerge);
  }
  @Test
  void shouldSaveMergedTag() {
    var tag = Tag.create("test");
    var tagToMerge = Tag.create("test 2");
    when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
    when(tagRepository.findById(tagToMerge.getId())).thenReturn(Optional.of(tagToMerge));

    handler.handle(tagToMerge.getId(), tag.getId());

    verify(tagRepository).save(assertArg(t -> {
      assertThat(t).isEqualTo(tag);
    }));
  }

}