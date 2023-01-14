package de.innovationhub.prox.modules.user.application.account.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.account.ProxUserAccount;
import de.innovationhub.prox.modules.user.domain.account.ProxUserAccountRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindByIdHandler {

  private final ProxUserAccountRepository proxUserAccountRepository;

  public Optional<ProxUserAccount> handle(UUID id) {
    return proxUserAccountRepository.findById(id);
  }
}