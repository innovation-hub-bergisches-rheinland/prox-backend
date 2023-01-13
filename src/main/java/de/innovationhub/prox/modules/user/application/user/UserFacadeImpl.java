package de.innovationhub.prox.modules.user.application.user;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.user.usecase.queries.FindByIdHandler;
import de.innovationhub.prox.modules.user.contract.user.ProxUserView;
import de.innovationhub.prox.modules.user.contract.user.UserFacade;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

  private final FindByIdHandler findById;

  @Override
  public Optional<ProxUserView> findById(UUID id) {
    return findById.handle(id)
        .map(u -> new ProxUserView(u.getId(), u.getName()));
  }
}
