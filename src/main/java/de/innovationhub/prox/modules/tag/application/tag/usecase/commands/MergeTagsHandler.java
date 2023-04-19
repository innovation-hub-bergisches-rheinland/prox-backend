package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class MergeTagsHandler {
  private final TagRepository tagRepository;
  private final TagDtoMapper tagDtoMapper;

  @Transactional
  @PreAuthorize("hasRole('admin')")
  public TagDto handle(UUID tagToMergeId, UUID targetTagId) {
    Tag tagToMerge = tagRepository.findById(tagToMergeId).orElseThrow();
    Tag targetTag = tagRepository.findById(targetTagId).orElseThrow();

    targetTag.merge(tagToMerge);
    tagRepository.delete(tagToMerge);
    tagRepository.save(targetTag);

    return tagDtoMapper.toDto(targetTag);
  }
}
