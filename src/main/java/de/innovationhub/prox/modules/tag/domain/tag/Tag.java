package de.innovationhub.prox.modules.tag.domain.tag;

import de.innovationhub.prox.Default;
import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
public class Tag extends AbstractAggregateRoot {

  @Id
  private UUID id;

  @NaturalId
  @Getter
  @NotBlank
  @Size(max = 128)
  private String tagName;

  public static Tag createNew(String tag) {
    var createdTag = new Tag(UUID.randomUUID(), tag);
    createdTag.registerEvent(TagCreated.from(createdTag));
    return createdTag;
  }

  @Default
  public Tag(UUID id, String tagName) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(tagName);

    if (tagName.isBlank()) {
      throw new RuntimeException("Tag cannot be blank");
    }

    this.id = id;
    this.tagName = tagName.toLowerCase();
  }
}
