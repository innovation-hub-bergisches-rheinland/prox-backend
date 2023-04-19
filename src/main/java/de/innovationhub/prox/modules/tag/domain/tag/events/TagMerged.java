package de.innovationhub.prox.modules.tag.domain.tag.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.Set;
import java.util.UUID;

public record TagMerged(
    TagMergedData mergedTag,
    TagMergedData targetTag
) implements DomainEvent {
  public record TagMergedData(UUID id, String tagName, Set<String> aliases) {}

  public static TagMerged from(Tag target, Tag merged) {
    return new TagMerged(
        new TagMergedData(merged.getId(), merged.getTagName(), merged.getAliases()),
        new TagMergedData(target.getId(), target.getTagName(), target.getAliases())
    );
  }
}
