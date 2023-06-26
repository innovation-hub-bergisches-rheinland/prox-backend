package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

class SplitTagsHandlerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  TagRepository tagRepository;

  @Autowired
  TagCollectionRepository tagCollectionRepository;

  @Autowired
  SplitTagsHandler splitTagsHandler;


  @Test
  @WithMockUser(roles = "admin")
  void shouldSplitTag() {
    var existingTag = Tag.create("Low-Code/No-Code");
    tagRepository.save(existingTag);

    splitTagsHandler.handle(existingTag.getId(), List.of("Low-Code", "No-Code"));

    var foundTags = tagRepository.findAllByTagNameInIgnoreCase(List.of("low-code", "no-code"));
    assertThat(foundTags)
        .hasSize(2);
  }

  @Test
  @WithMockUser(roles = "admin")
  void shouldDeleteSplittedTag() {
    var existingTag = Tag.create("Low-Code/No-Code");
    tagRepository.save(existingTag);

    splitTagsHandler.handle(existingTag.getId(), List.of("Low-Code", "No-Code"));
    var found = tagRepository.findById(existingTag.getId());
    assertThat(found)
        .isEmpty();
  }

  @Test
  @WithMockUser(roles = "admin")
  void shouldUpdateTagCollections() {
    var existingTag = Tag.create("Low-Code/No-Code");
    var tagCollection = TagCollection.create(UUID.randomUUID(), List.of(existingTag));
    tagRepository.save(existingTag);
    tagCollectionRepository.save(tagCollection);

    splitTagsHandler.handle(existingTag.getId(), List.of("Low-Code", "No-Code"));

    var foundTagCollection = tagCollectionRepository.findById(tagCollection.getId());
    assertThat(foundTagCollection)
        .isPresent()
        .get()
        .extracting(TagCollection::getTags)
        .satisfies(tl -> {
          assertThat(tl)
              .extracting(Tag::getTagName)
              .containsExactly("low-code", "no-code");
        });
  }

  @Test
  @WithMockUser(roles = "admin")
  void shouldNotAffectOtherTagsInCollection() {
    var existingTag = Tag.create("Low-Code/No-Code");
    var otherTag = Tag.create("test");
    var tagCollection = TagCollection.create(UUID.randomUUID(), List.of(existingTag, otherTag));
    tagRepository.save(existingTag);
    tagRepository.save(otherTag);
    tagCollectionRepository.save(tagCollection);

    splitTagsHandler.handle(existingTag.getId(), List.of("Low-Code", "No-Code"));

    var found = tagCollectionRepository.findById(tagCollection.getId());
    assertThat(found.get().getTags())
        .contains(otherTag);
  }
}