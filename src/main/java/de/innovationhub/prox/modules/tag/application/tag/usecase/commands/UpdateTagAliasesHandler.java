package de.innovationhub.prox.modules.tag.application.tag.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateTagAliasesHandler {
  private final TagRepository tagRepository;
  private final TagDtoMapper tagDtoMapper;

  @Transactional
  @PreAuthorize("hasRole('admin')")
  public TagDto handle(UUID tagId, Set<String> aliases) {
    Tag tag = tagRepository.findById(tagId).orElseThrow();

    tag.updateAliases(aliases);
    tagRepository.save(tag);

    return tagDtoMapper.toDto(tag);
  }
}
