package de.innovationhub.prox.modules.user.application.account;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.account.usecase.queries.FindByIdHandler;
import de.innovationhub.prox.modules.user.contract.account.ProxUserAccountView;
import de.innovationhub.prox.modules.user.contract.account.UserAccountFacade;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UserAccountFacadeImpl implements UserAccountFacade {

  private final FindByIdHandler findById;

  @Override
  public Optional<ProxUserAccountView> findById(UUID id) {
    return findById.handle(id)
        .map(u -> new ProxUserAccountView(u.getId(), u.getName(), u.getEmail()));
  }
}
