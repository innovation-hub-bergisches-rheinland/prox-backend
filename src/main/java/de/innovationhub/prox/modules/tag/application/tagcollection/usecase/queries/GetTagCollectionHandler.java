package de.innovationhub.prox.modules.tag.application.tagcollection.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.application.tagcollection.dto.TagCollectionDtoMapper;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tag.TagRepository;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollectionRepository;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class GetTagCollectionHandler {
  private final TagCollectionRepository tagCollectionRepository;
  private final TagCollectionDtoMapper tagCollectionDtoMapper;

  public Optional<TagCollectionDto> handle(UUID id) {
    return tagCollectionRepository.findById(id)
        .map(tagCollectionDtoMapper::toDto);
  }
}
