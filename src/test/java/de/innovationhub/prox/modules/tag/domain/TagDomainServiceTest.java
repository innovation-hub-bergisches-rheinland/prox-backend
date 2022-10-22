package de.innovationhub.prox.modules.tag.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TagDomainServiceTest {
  TagCollectionRepository tagCollectionRepository = mock(TagCollectionRepository.class);
  TagRepository tagRepository = mock(TagRepository.class);
  TagDomainService tagDomainService = new TagDomainService(tagRepository, tagCollectionRepository);

  @Test
  void shouldCreateCollection() {
    var inputTags = List.of("tag1", "tag2");
    when(tagCollectionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    setupMockedTags(inputTags);

    var tagCollection = tagDomainService.createCollection(inputTags);

    assertThat(tagCollection)
        .satisfies(tc -> {
          assertThat(tc.getTags())
              .hasSameSizeAs(inputTags);
        });
    verify(tagCollectionRepository).save(tagCollection);
  }

  @Test
  void shouldUpdateCollection() {
    var tagCollectionId = UUID.randomUUID();
    var inputTags = List.of("tag1", "tag2");
    when(tagCollectionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    setupMockedTagCollection(tagCollectionId);
    setupMockedTags(inputTags);

    var tagCollection = tagDomainService.updateCollection(tagCollectionId, inputTags);

    assertThat(tagCollection)
        .satisfies(tc -> {
          assertThat(tc.getTags())
              .hasSameSizeAs(inputTags);
        });
    verify(tagCollectionRepository).save(tagCollection);
  }

  private void setupMockedTags(Collection<String> tags) {
    var mockedTags = tags
        .stream()
        .map(Tag::createNew)
        .toList();
    when(tagRepository.fetchOrCreateTags(anyCollection())).thenReturn(mockedTags);
  }

  private void setupMockedTagCollection(UUID tagCollectionId) {
    var tc = new TagCollection(tagCollectionId, new ArrayList<>());
    when(tagCollectionRepository.findById(tagCollectionId)).thenReturn(Optional.of(tc));
  }
}