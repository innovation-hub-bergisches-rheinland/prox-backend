package de.innovationhub.prox.modules.tag.application.tagcollection.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.tag.application.tagcollection.dto.TagCollectionDtoMapper;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.SetTagCollectionHandler;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetTagCollectionHandlerTest {
  TagCollectionRepository tagCollectionRepository = mock(TagCollectionRepository.class);
  TagRepository tagRepository = mock(TagRepository.class);
  SetTagCollectionHandler handler = new SetTagCollectionHandler(tagCollectionRepository, tagRepository, TagCollectionDtoMapper.INSTANCE);

  @Test
  void shouldCreateTagCollection() {
    var id = UUID.randomUUID();
    when(tagCollectionRepository.findById(id)).thenReturn(Optional.empty());
    handler.handle(id, new ArrayList<>());

    verify(tagCollectionRepository).save(assertArg(tc -> {
      assertThat(tc.getId()).isEqualTo(id);
    }));
  }

  @Test
  void shouldUpdateTagCollection() {
    var id = UUID.randomUUID();
    var tagCollection = new TagCollection(id);

    when(tagCollectionRepository.findById(id)).thenReturn(Optional.of(tagCollection));
    handler.handle(id, new ArrayList<>());

    verify(tagCollectionRepository).save(assertArg(tc -> {
      assertThat(tc.getId()).isEqualTo(id);
    }));
  }
}