package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;

public record FindPopularTags(int limit) implements UseCase {
  public FindPopularTags {
    if(limit <= 0) {
      throw new IllegalArgumentException("Limit must be greater than 0");
    }
  }
}
