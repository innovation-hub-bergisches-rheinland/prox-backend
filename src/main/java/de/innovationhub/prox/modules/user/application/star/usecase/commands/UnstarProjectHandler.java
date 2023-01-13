package de.innovationhub.prox.modules.user.application.star.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.user.exception.UserNotFoundException;
import de.innovationhub.prox.modules.user.domain.star.StarCollectionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class UnstarProjectHandler {

  private final StarCollectionRepository starCollectionRepository;

  public void handle(UUID userId, UUID projectId) {
    var user = starCollectionRepository.findByUserId(userId)
        .orElseThrow(UserNotFoundException::new);
    user.unstarProject(projectId);
    starCollectionRepository.save(user);
  }
}
