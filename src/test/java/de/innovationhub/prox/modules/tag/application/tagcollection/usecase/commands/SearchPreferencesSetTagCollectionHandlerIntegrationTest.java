package de.innovationhub.prox.modules.tag.application.tagcollection.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
class SearchPreferencesSetTagCollectionHandlerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  SetTagCollectionHandler setTagCollectionHandler;

  @Autowired
  TagRepository tagRepository;

  @Autowired
  TagCollectionRepository tagCollectionRepository;

  @Test
  void shouldSetTags() {
    var tags = List.of(Tag.create("test1"), Tag.create("test2"));
    tagRepository.saveAll(tags);
    var tagIds = tags.stream().map(Tag::getId).toList();
    var id = UUID.randomUUID();

    setTagCollectionHandler.handle(id, tagIds);

    var tagCollection = tagCollectionRepository.findById(id).orElseThrow();
    assertThat(tagCollection.getTags())
        .extracting(Tag::getId)
        .containsExactlyElementsOf(tagIds);
  }

  @Test
  void shouldSetTagCollectionPartially() {
    var tag1 = Tag.create("test1");
    var tag2 = Tag.create("test2");
    var tags = List.of(tag1, tag2);

    // Only the first tag is being saved
    tagRepository.save(tag1);

    var tagIds = tags.stream().map(Tag::getId).toList();
    var id = UUID.randomUUID();

    // We are still going to set both tags
    setTagCollectionHandler.handle(id, tagIds);

    var tagCollection = tagCollectionRepository.findById(id).orElseThrow();

    // Only the saved one is applied, no exception is thrown
    assertThat(tagCollection.getTags())
        .extracting(Tag::getId)
        .containsExactlyElementsOf(List.of(tag1.getId()));
  }
}