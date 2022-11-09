package de.innovationhub.prox.modules.tag.domain.tag;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TagRepositoryTest extends AbstractIntegrationTest {
  @Autowired
  TagRepository tagRepository;

  @Autowired
  EntityManager entityManager;

  @Test
  void shouldCreateMissingTag() {
    var tagInput = List.of("test1", "test2");
    var returnedTags = tagRepository.fetchOrCreateTags(tagInput);

    assertThat(returnedTags)
        .extracting(Tag::getTagName)
        .containsExactlyElementsOf(tagInput);
    assertThat(tagRepository.existsByTagName("test1"))
        .isTrue();
    assertThat(tagRepository.existsByTagName("test2"))
        .isTrue();
  }

  @Test
  void shouldReturnTags() {
    var givenTags = List.of(Tag.create("test3"), Tag.create("test4"));
    tagRepository.saveAll(givenTags);

    var tagInput = List.of("test3", "test4");
    var returnedTags = tagRepository.fetchOrCreateTags(tagInput);

    assertThat(returnedTags)
        .extracting(Tag::getTagName)
        .containsExactlyElementsOf(tagInput);
  }
}