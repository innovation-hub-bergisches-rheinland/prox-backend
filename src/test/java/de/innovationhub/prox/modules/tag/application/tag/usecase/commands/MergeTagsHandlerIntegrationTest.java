package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.ClearDatabase;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagMerged;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.transaction.TestTransaction;

@RecordApplicationEvents
class MergeTagsHandlerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  MergeTagsHandler mergeTagsHandler;

  @Autowired
  ApplicationEvents applicationEvents;

  @Autowired
  TagRepository tagRepository;

  @Autowired
  TagCollectionRepository tagCollectionRepository;

  @Test
  @WithAnonymousUser
  void shoulThrowOnUnauthorized() {
    assertThrows(AccessDeniedException.class, () -> mergeTagsHandler.handle(UUID.randomUUID(), UUID.randomUUID()));
  }

  @Test
  @WithMockUser(roles = "admin")
  void shouldPublishTagMergedEvent() {
    var tag = Tag.create("test");
    var tagToMerge = Tag.create("test 2");
    tagRepository.save(tag);
    tagRepository.save(tagToMerge);

    mergeTagsHandler.handle(tagToMerge.getId(), tag.getId());

    assertThat(applicationEvents.stream(TagMerged.class))
        .hasSize(1)
        .first()
        .satisfies(event -> {
          assertThat(event.mergedTag().id()).isEqualTo(tagToMerge.getId());
          assertThat(event.targetTag().id()).isEqualTo(tag.getId());
        });
  }

  @Test
  @WithMockUser(roles = "admin")
  void shouldUpdateTagsInTagCollection() {
    var tag = Tag.create("test");
    var tagToMerge = Tag.create("test 2");
    var tags = Set.of(tag, tagToMerge);
    var tagCollection = TagCollection.create(UUID.randomUUID(), tags);
    tagRepository.saveAll(tags);
    tagCollectionRepository.save(tagCollection);

    mergeTagsHandler.handle(tagToMerge.getId(), tag.getId());

    var savedTagCollection = tagCollectionRepository.findById(tagCollection.getId()).get();
    assertThat(savedTagCollection)
        .extracting(TagCollection::getTags, as(InstanceOfAssertFactories.collection(Tag.class)))
        .hasSize(1)
        .first()
        .extracting(Tag::getId)
        .isEqualTo(tag.getId());
  }

}