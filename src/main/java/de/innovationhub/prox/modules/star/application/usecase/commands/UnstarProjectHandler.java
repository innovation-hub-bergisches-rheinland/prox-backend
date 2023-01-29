package de.innovationhub.prox.modules.star.application.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.star.domain.StarCollectionRepository;
import de.innovationhub.prox.modules.user.application.user.exception.UserNotFoundException;
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
