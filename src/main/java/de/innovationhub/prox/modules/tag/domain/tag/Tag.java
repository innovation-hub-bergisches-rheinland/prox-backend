package de.innovationhub.prox.modules.tag.domain.tag;

import de.innovationhub.prox.Default;
import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

/**
 * A Tag is a semantic annotation
 */
@Data
public class Tag extends AbstractAggregateRoot {

  private final UUID id;

  @Getter
  @NotBlank
  @Size(max = 128)
  private final String tag;

  public static Tag createNew(String tag) {
    var createdTag = new Tag(UUID.randomUUID(), tag);
    createdTag.registerEvent(TagCreated.from(createdTag));
    return createdTag;
  }

  public Tag(String tag) {
    this(UUID.randomUUID(), tag);
  }

  @Default
  public Tag(UUID id, String tag) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(tag);

    if (tag.isBlank()) {
      throw new RuntimeException("Tag cannot be blank");
    }

    this.id = id;
    this.tag = tag.toLowerCase();
  }
}
