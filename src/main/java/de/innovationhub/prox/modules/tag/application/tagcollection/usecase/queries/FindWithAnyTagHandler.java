package de.innovationhub.prox.modules.tag.application.tagcollection.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tagcollection.dto.TagCollectionDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindWithAnyTagHandler {
  private final TagCollectionRepository tagCollectionRepository;
  private final TagCollectionDtoMapper dtoMapper;

  public List<TagCollectionDto> handle(Collection<UUID> tags) {
    if(tags.isEmpty()) {
      return List.of();
    }
    return tagCollectionRepository.findWithAnyTag(tags)
        .stream()
        .map(dtoMapper::toDto)
        .toList();
  }
}
