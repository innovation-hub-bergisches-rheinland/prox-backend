package de.innovationhub.prox.modules.tag.application.tag;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.contract.TagView;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TagFacadeImplIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  TagRepository tagRepository;

  @Autowired
  TagFacadeImpl tagFacadeImpl;

  @AfterEach
  void tearDown() {
    tagRepository.deleteAll();
  }

  @Test
  void shouldFindTags() {
    var tag1 = Tag.create("tag1");
    var tag2 = Tag.create("tag2");
    tagRepository.saveAll(List.of(tag1, tag2));

    var result = tagFacadeImpl.getTags(List.of(tag1.getId(), tag2.getId()));

    assertThat(result).containsExactlyInAnyOrder("tag1", "tag2");
  }

  @Test
  void shouldFindTagsByName() {
    var tag1 = Tag.create("tag1");
    var tag2 = Tag.create("tag2");
    tagRepository.saveAll(List.of(tag1, tag2));

    var result = tagFacadeImpl.getTagsByName(List.of(tag1.getTagName(), tag2.getTagName()));

    assertThat(result)
        .extracting(TagView::tagName)
        .containsExactlyInAnyOrder("tag1", "tag2");
  }
}