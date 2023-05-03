package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.application.tag.dto.UpdateTagRequest;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeleteTagHandlerTest {
  TagRepository tagRepository = mock(TagRepository.class);
  TagCollectionRepository tagCollectionRepository = mock(TagCollectionRepository.class);
  DeleteTagHandler handler = new DeleteTagHandler(tagRepository, tagCollectionRepository);

  @Test
  void shouldDelete() {
    var tag = Tag.create("test");
    when(tagRepository.findById(tag.getId())).thenReturn(Optional.of(tag));

    handler.handle(tag.getId());

    verify(tagCollectionRepository).deleteAllTags(tag.getId());
    verify(tagRepository).delete(tag);
  }

}