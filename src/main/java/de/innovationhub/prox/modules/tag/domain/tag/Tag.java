package de.innovationhub.prox.modules.tag.domain.tag;

import de.innovationhub.prox.commons.Default;
import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * A Tag is a semantic annotation
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(schema = PersistenceConfig.TAG_SCHEMA)
public class Tag extends AuditedAggregateRoot {

  @Id
  private UUID id;

  @NaturalId
  @Getter
  @NotBlank
  @Size(max = 128)
  @Column(unique = true, nullable = false, length = 128)
  private String tagName;

  public static Tag create(String tag) {
    var createdTag = new Tag(UUID.randomUUID(), tag);
    createdTag.registerEvent(TagCreated.from(createdTag));
    return createdTag;
  }

  @Default
  public Tag(UUID id, String tagName) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(tagName);

    if (tagName.isBlank()) {
      throw new IllegalArgumentException("Tag cannot be blank");
    }

    this.id = id;
    this.tagName = tagName.toLowerCase();
  }
}
