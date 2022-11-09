package de.innovationhub.prox.modules.tag.application.tagcollection.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
  SetTagCollectionHandler handler = new SetTagCollectionHandler(tagCollectionRepository, tagRepository);

  @Test
  void shouldCreateTagCollection() {
    var id = UUID.randomUUID();
    when(tagCollectionRepository.findById(id)).thenReturn(Optional.empty());
    handler.handle(id, new ArrayList<>());

    ArgumentCaptor<TagCollection> captor = ArgumentCaptor.forClass(TagCollection.class);
    verify(tagCollectionRepository).save(captor.capture());
    assertEquals(id, captor.getValue().getId());
  }

  @Test
  void shouldUpdateTagCollection() {
    var id = UUID.randomUUID();
    var tagCollection = new TagCollection(id);

    when(tagCollectionRepository.findById(id)).thenReturn(Optional.of(tagCollection));
    handler.handle(id, new ArrayList<>());

    ArgumentCaptor<TagCollection> captor = ArgumentCaptor.forClass(TagCollection.class);
    verify(tagCollectionRepository).save(captor.capture());
    var captorValue = captor.getValue();
    assertEquals(id, captorValue.getId());
  }
}