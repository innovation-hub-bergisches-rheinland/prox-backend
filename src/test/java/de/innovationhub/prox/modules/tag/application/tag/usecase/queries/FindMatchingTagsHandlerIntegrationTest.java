package de.innovationhub.prox.modules.tag.application.tag.usecase.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

class FindMatchingTagsHandlerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  TagRepository tagRepository;

  @Autowired
  FindMatchingTagsHandler findMatchingTagsHandler;

  @Test
  void shouldFindByAlias() {
    var tag = Tag.create("test");
    tag.updateAliases(Set.of("an-alias-of-test"));
    tagRepository.save(tag);

    var result = findMatchingTagsHandler.handle("alias", Pageable.unpaged());
    assertThat(result)
        .hasSize(1)
        .first()
        .isEqualTo(tag);
  }

}