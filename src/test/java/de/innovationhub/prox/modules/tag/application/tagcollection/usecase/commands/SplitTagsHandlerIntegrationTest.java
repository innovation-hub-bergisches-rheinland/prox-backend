package de.innovationhub.prox.modules.tag.application.tagcollection.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.application.tagcollection.usecase.commands.SplitTagsHandler;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Arrays;
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
    var tagsToSplitInTo = createTags("Low-Code", "No-Code");
    var tagsToSplitInToIds = tagsToSplitInTo.stream()
            .map(Tag::getId)
            .toList();
    var tagCollection = TagCollection.create(UUID.randomUUID(), List.of(existingTag));
    tagRepository.save(existingTag);
    tagRepository.saveAll(tagsToSplitInTo);
    tagCollectionRepository.save(tagCollection);

    splitTagsHandler.handle(existingTag.getId(), tagsToSplitInToIds);

    var foundTagCollection = tagCollectionRepository.findById(tagCollection.getId());
    assertThat(foundTagCollection.get().getTags())
        .extracting(Tag::getTagName)
        .contains("low-code", "no-code");
  }

  @Test
  @WithMockUser(roles = "admin")
  void shouldNotAffectOtherTagsInCollection() {
    var existingTag = Tag.create("Low-Code/No-Code");
    var otherTag = Tag.create("otherTag");
    var tagsToSplitInTo = createTags("Low-Code", "No-Code");
    var tagsToSplitInToIds = tagsToSplitInTo.stream()
        .map(Tag::getId)
        .toList();
    var tagCollection = TagCollection.create(UUID.randomUUID(), List.of(existingTag, otherTag));
    tagRepository.save(existingTag);
    tagRepository.save(otherTag);
    tagRepository.saveAll(tagsToSplitInTo);
    tagCollectionRepository.save(tagCollection);

    splitTagsHandler.handle(existingTag.getId(), tagsToSplitInToIds);

    var found = tagCollectionRepository.findById(tagCollection.getId());
    assertThat(found.get().getTags())
        .contains(otherTag);
  }

  private List<Tag> createTags(String... tags) {
    return Arrays.stream(tags).map(Tag::create).toList();
  }
}