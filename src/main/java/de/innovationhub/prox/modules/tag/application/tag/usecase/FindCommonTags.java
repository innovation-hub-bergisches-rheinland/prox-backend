package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.Collection;
import java.util.Objects;

public record FindCommonTags(Collection<String> tags, int limit) implements UseCase {
  public FindCommonTags {
    Objects.requireNonNull(tags);

    if(tags.isEmpty()) {
      throw new IllegalArgumentException("Must provide at least one tag");
    }
    if(limit <= 0) {
      throw new IllegalArgumentException("Limit must be greater than 0");
    }
  }
}
