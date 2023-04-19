package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import jakarta.transaction.Transactional.TxType;
import java.util.UUID;
import jakarta.transaction.Transactional;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class MergeTagsHandler {
  private final TagRepository tagRepository;
  private final TagCollectionRepository tagCollectionRepository;
  private final TagDtoMapper tagDtoMapper;

  @Transactional
  @PreAuthorize("hasRole('admin')")
  public TagDto handle(UUID tagToMergeId, UUID targetTagId) {
    Tag tagToMerge = tagRepository.findById(tagToMergeId).orElseThrow();
    Tag targetTag = tagRepository.findById(targetTagId).orElseThrow();

    targetTag.merge(tagToMerge);
    // We use the tag collection repository directly instead of firing an event
    // because we are in the same module boundary. We consider this service more of a domain service
    // than a use case. However, to keep things consistent and to avoid confusion, we still use the
    // handler terminology and logic for this.
    tagCollectionRepository.replaceAllTags(tagToMerge.getId(), targetTag.getId());
    tagCollectionRepository.deleteAllTags(tagToMerge.getId());
    tagRepository.save(targetTag);
    tagRepository.delete(tagToMerge);

    return tagDtoMapper.toDto(targetTag);
  }
}
