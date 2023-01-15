package de.innovationhub.prox.modules.star.application.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.star.domain.StarCollectionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class CheckProjectStarHandler {
  private final StarCollectionRepository starCollectionRepository;

  public boolean handle(UUID userId, UUID projectId) {
    return starCollectionRepository.existsByUserIdAndStarredProjectsContains(userId, projectId);
  }
}