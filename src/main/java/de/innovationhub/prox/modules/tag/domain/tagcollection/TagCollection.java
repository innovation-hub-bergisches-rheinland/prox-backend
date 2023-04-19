package de.innovationhub.prox.modules.tag.domain.tagcollection;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionCreated;
import de.innovationhub.prox.modules.tag.domain.tagcollection.events.TagCollectionUpdated;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(schema = PersistenceConfig.TAG_SCHEMA)
public class TagCollection extends AuditedAggregateRoot {

  @Id
  private UUID id;

  @ManyToMany(fetch = FetchType.EAGER)
  @CollectionTable(schema = PersistenceConfig.TAG_SCHEMA)
  private List<Tag> tags = new ArrayList<>();

  public static TagCollection create(UUID id) {
    return create(id, new ArrayList<>());
  }

  public static TagCollection create(UUID id, Collection<Tag> tags) {
    var createdTagCollection = new TagCollection(id, new ArrayList<>(tags));
    createdTagCollection.registerEvent(TagCollectionCreated.from(createdTagCollection));
    return createdTagCollection;
  }

  public TagCollection(UUID id, List<Tag> tags) {
    this.id = id;
    this.tags = tags;
  }

  public void setTags(Collection<Tag> tags) {
    this.tags = new ArrayList<>(tags);
    this.registerEvent(TagCollectionUpdated.from(this));
  }
}
