package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.application.tag.dto.UpdateTagRequest;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class DeleteTagHandler {
  private final TagRepository tagRepository;
  private final TagCollectionRepository tagCollectionRepository;

  @Transactional
  @PreAuthorize("hasRole('admin')")
  public void handle(UUID tagId) {
    Tag tag = tagRepository.findById(tagId).orElseThrow();

    tagCollectionRepository.deleteAllTags(tag.getId());
    tagRepository.delete(tag);
  }
}
