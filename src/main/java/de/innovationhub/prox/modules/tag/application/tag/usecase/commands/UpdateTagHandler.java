package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.application.tag.dto.UpdateTagRequest;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateTagHandler {
  private final TagRepository tagRepository;
  private final TagDtoMapper tagDtoMapper;

  @Transactional
  @PreAuthorize("hasRole('admin')")
  public TagDto handle(UUID tagId, UpdateTagRequest request) {
    Tag tag = tagRepository.findById(tagId).orElseThrow();

    tag.update(request.tagName(), request.aliases());
    tagRepository.save(tag);

    return tagDtoMapper.toDto(tag);
  }
}
