package de.innovationhub.prox.modules.tag.domain.tag;

import de.innovationhub.prox.Default;
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
public class Tag {

  private final UUID id;

  @Getter
  @NotBlank
  @Size(max = 128)
  private final String tag;

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
