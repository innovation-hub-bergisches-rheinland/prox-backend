package de.innovationhub.prox.modules.tag.domain.tagcollection;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionCreated;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionUpdated;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

  @ManyToMany
  private List<Tag> tags = new ArrayList<>();

  public static TagCollection create(UUID id) {
    var createdTagCollection = new TagCollection(id);
    createdTagCollection.registerEvent(TagCollectionCreated.from(createdTagCollection));
    return createdTagCollection;
  }

  public TagCollection(UUID id) {
    this.id = id;
  }

  public void setTags(Collection<Tag> tags) {
    this.tags = new ArrayList<>(tags);
    this.registerEvent(TagCollectionUpdated.from(this));
  }
}
