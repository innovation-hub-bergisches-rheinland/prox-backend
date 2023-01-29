package de.innovationhub.prox.modules.user.application.user.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.user.ProxUser;
import de.innovationhub.prox.modules.user.domain.user.ProxUserAccountRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindUserAccountByIdHandler {

  private final ProxUserAccountRepository proxUserAccountRepository;

  public Optional<ProxUser> handle(UUID id) {
    return proxUserAccountRepository.findById(id);
  }
}