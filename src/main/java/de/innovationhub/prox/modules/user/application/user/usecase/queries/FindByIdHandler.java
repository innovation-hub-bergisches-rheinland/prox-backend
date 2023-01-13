package de.innovationhub.prox.modules.user.application.user.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.user.ProxUser;
import de.innovationhub.prox.modules.user.domain.user.ProxUserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindByIdHandler {

  private final ProxUserRepository proxUserRepository;

  public Optional<ProxUser> handle(UUID id) {
    return proxUserRepository.findById(id);
  }
}