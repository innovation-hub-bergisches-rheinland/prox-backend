package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.*;

class UpdateTagAliasesHandlerTest {
  TagRepository tagRepository = mock(TagRepository.class);
  TagDtoMapper tagDtoMapper = TagDtoMapper.INSTANCE;
  UpdateTagAliasesHandler handler = new UpdateTagAliasesHandler(tagRepository, tagDtoMapper);


  @Test
  void shouldUpdateTagAliases() {
    var tag = Tag.create("test");
    var aliases = Set.of("test", "test-2");
    when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));

    handler.handle(tag.getId(), aliases);

    verify(tagRepository).save(assertArg(t -> {
      assertThat(t.getAliases()).containsExactlyInAnyOrderElementsOf(aliases);
    }));
  }

}