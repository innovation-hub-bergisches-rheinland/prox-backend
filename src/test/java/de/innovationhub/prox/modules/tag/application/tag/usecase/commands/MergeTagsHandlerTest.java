package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import org.junit.jupiter.api.BeforeEach;
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
  TagCollectionRepository tagCollectionRepository = mock(TagCollectionRepository.class);
  TagDtoMapper tagDtoMapper = TagDtoMapper.INSTANCE;
  MergeTagsHandler handler = new MergeTagsHandler(tagRepository, tagCollectionRepository, tagDtoMapper);


  Tag tag = Tag.create("test");
  Tag tagToMerge = Tag.create("test 2");

  @BeforeEach
  void setUp() {
    when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));
    when(tagRepository.findById(tagToMerge.getId())).thenReturn(Optional.of(tagToMerge));
  }


  @Test
  void shouldDeleteMergedTag() {
    handler.handle(tagToMerge.getId(), tag.getId());

    verify(tagRepository).delete(tagToMerge);
  }

  @Test
  void shouldReplaceMergedTag() {
    handler.handle(tagToMerge.getId(), tag.getId());

    verify(tagCollectionRepository).replaceAllTags(tagToMerge.getId(), tag.getId());
    verify(tagCollectionRepository).deleteAllTags(tagToMerge.getId());
  }

  @Test
  void shouldSaveMergedTag() {
    handler.handle(tagToMerge.getId(), tag.getId());

    verify(tagRepository).save(assertArg(t -> {
      assertThat(t).isEqualTo(tag);
    }));
  }

}