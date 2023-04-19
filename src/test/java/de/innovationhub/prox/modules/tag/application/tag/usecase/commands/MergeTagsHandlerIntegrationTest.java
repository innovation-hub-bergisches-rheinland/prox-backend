package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagMerged;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.*;

@RecordApplicationEvents
@Transactional
class MergeTagsHandlerIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  MergeTagsHandler mergeTagsHandler;

  @Autowired
  ApplicationEvents applicationEvents;

  @Autowired
  TagRepository tagRepository;

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

}