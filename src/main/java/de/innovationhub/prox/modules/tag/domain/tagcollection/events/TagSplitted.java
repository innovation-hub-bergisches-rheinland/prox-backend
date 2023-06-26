package de.innovationhub.prox.modules.tag.domain.tagcollection.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.Set;
import java.util.UUID;

public record TagSplitted(
    UUID id,
    Tag tagToSplit,
    Set<Tag> splittedTags
) implements DomainEvent {
}
