package de.innovationhub.prox.modules.star.application.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.star.domain.StarCollection;
import de.innovationhub.prox.modules.star.domain.StarCollectionRepository;
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
