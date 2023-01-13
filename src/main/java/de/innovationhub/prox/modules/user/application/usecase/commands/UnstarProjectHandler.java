package de.innovationhub.prox.modules.user.application.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.exception.UserNotFoundException;
import de.innovationhub.prox.modules.user.domain.ProxUserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class UnstarProjectHandler {
  private final ProxUserRepository userRepository;

  public void handle(UUID userId, UUID projectId) {
    var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    user.unstarProject(projectId);
    userRepository.save(user);
  }
}
