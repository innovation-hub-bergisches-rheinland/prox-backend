package de.innovationhub.prox.modules.tag.contract;

import java.util.List;
import java.util.UUID;

public interface TagCollectionFacade {
  TagCollectionView get(UUID id);
  TagCollectionView set(UUID id, List<String> tags);
}
