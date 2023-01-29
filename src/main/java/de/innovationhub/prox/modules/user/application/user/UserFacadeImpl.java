package de.innovationhub.prox.modules.user.application.user;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindUserProfileHandler;
import de.innovationhub.prox.modules.user.application.user.usecase.queries.FindUserAccountByIdHandler;
import de.innovationhub.prox.modules.user.contract.user.ProxUserView;
import de.innovationhub.prox.modules.user.contract.user.UserFacade;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

  private final FindUserAccountByIdHandler findById;
  private final FindUserProfileHandler findUserProfile;

  @Override
  public Optional<ProxUserView> findById(UUID id) {
    // We first try to find the profile to ensure the correct name resolution
    var profile = findUserProfile.handle(id);
    if (profile.isPresent()) {
      return profile.map(p -> new ProxUserView(p.getId(), p.getDisplayName()));
    }

    // Otherwise we try to find the user account
    return findById.handle(id).map(u -> new ProxUserView(u.getId(), u.getName()));
  }
}
