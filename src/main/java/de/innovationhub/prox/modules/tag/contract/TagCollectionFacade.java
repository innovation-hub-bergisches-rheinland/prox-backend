package de.innovationhub.prox.modules.tag.contract;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationComponent
public interface TagCollectionFacade {
  Optional<TagCollectionDto> getTagCollection(UUID id);
  TagCollectionDto setTagCollection(UUID id, Collection<UUID> tags);
  List<TagCollectionDto> findWithAnyTag(Collection<UUID> tags);
  List<TagCollectionDto> findWithAllTags(Collection<UUID> tags);
}
