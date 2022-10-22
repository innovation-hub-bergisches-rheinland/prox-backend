package de.innovationhub.prox.modules.tag.domain.tagcollection;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionCreated;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionUpdated;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class TagCollection extends AbstractAggregateRoot {
  @Id
  private UUID id;

  @ElementCollection
  private List<UUID> tags;

  public static TagCollection create(UUID id, Collection<UUID> tags) {
    var createdTagCollection = new TagCollection(id, List.copyOf(tags));
    createdTagCollection.registerEvent(TagCollectionCreated.from(createdTagCollection));
    return createdTagCollection;
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new ArrayList<>(tags);
    this.registerEvent(TagCollectionUpdated.from(this));
  }
}
