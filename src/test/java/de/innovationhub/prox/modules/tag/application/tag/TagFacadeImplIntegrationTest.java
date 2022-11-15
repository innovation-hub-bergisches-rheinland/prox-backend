package de.innovationhub.prox.modules.tag.application.tag;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TagFacadeImplIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  TagRepository tagRepository;

  @Autowired
  TagFacadeImpl tagFacadeImpl;

  @Test
  void shouldFindTags() {
    var tag1 = Tag.create("tag1");
    var tag2 = Tag.create("tag2");
    tagRepository.saveAll(List.of(tag1, tag2));

    var result = tagFacadeImpl.getTags(List.of(tag1.getId(), tag2.getId()));

    assertThat(result).containsExactlyInAnyOrder("tag1", "tag2");
  }
}