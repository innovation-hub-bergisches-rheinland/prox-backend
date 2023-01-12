package de.innovationhub.prox.modules.auth.application.usecase.commands;

import de.innovationhub.prox.modules.auth.application.exception.UserNotFoundException;
import de.innovationhub.prox.modules.auth.domain.ProxUserRepository;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class StarProjectHandler {
  private final ProxUserRepository userRepository;

  public void handle(UUID userId, UUID projectId) {
    var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    user.starProject(projectId);
    userRepository.save(user);
  }
}
