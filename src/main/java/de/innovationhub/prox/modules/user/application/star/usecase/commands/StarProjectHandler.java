package de.innovationhub.prox.modules.user.application.star.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.star.StarCollection;
import de.innovationhub.prox.modules.user.domain.star.StarCollectionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class StarProjectHandler {

  private final StarCollectionRepository starCollectionRepository;

  public void handle(UUID userId, UUID projectId) {
    var user = starCollectionRepository.findByUserId(userId)
        .orElse(new StarCollection(userId));
    user.starProject(projectId);
    starCollectionRepository.save(user);
  }
}
