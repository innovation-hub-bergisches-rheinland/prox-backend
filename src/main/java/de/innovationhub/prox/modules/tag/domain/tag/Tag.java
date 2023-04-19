package de.innovationhub.prox.modules.tag.domain.tag;

import de.innovationhub.prox.commons.Default;
import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagMerged;
import de.innovationhub.prox.utils.StringUtils;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

/**
 * A Tag is a semantic annotation
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(schema = PersistenceConfig.TAG_SCHEMA)
@EqualsAndHashCode(callSuper = false)
@ToString
public class Tag extends AuditedAggregateRoot {

  @Id
  private UUID id;

  @NaturalId
  @Getter
  @NotBlank
  @Size(max = 128)
  @Column(unique = true, nullable = false, length = 128)
  private String tagName;

  @Getter
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(schema = PersistenceConfig.TAG_SCHEMA)
  private Set<String> aliases = new HashSet<>();

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
    this.tagName = StringUtils.slugify(tagName);
  }

  public void merge(Tag other) {
    Objects.requireNonNull(other);

    if (this.equals(other)) {
      throw new IllegalArgumentException("Cannot merge tags with itself");
    }

    // If we merge tags that are equivalent, we don't need to add the other tag's aliases
    if(!this.isEquivalent(other)) {
      this.aliases.add(other.tagName);
    }
    // However, it is safe to add the other tag's aliases to this tag
    this.aliases.addAll(other.aliases);
    this.registerEvent(TagMerged.from(this, other));
  }

  public void updateAliases(Set<String> aliases) {
    Objects.requireNonNull(aliases);

    var sluggedAliases = aliases.stream()
        .map(StringUtils::slugify)
        .toList();
    this.aliases = new HashSet<>(sluggedAliases);
  }

  public boolean isEquivalent(Tag other) {
    return this.tagName.equals(other.tagName);
  }
}
