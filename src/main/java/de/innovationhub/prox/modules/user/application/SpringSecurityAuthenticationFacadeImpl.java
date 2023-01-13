package de.innovationhub.prox.modules.user.application;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.exception.UnauthenticatedException;
import de.innovationhub.prox.modules.user.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.user.contract.ProxUserView;
import de.innovationhub.prox.modules.user.domain.ProxUserRepository;
import io.micrometer.core.lang.Nullable;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@ApplicationComponent
public class SpringSecurityAuthenticationFacadeImpl implements AuthenticationFacade {

  private final ProxUserRepository userRepository;

  @Override
  public UUID currentAuthenticatedId() {
    var auth = getAuthentication();

    if (auth == null) {
      throw new UnauthenticatedException();
    }

    // Name should always be a UUID. Investigate a better way.
    return UUID.fromString(auth.getName());
  }

  @Override
  public @Nullable Authentication getAuthentication() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      return null;
    }

    return auth;
  }

  @Override
  public ProxUserView getAuthenticatedUser() {
    return userRepository
        .findById(currentAuthenticatedId())
        .map(u -> new ProxUserView(u.getId()))
        .orElseThrow();
  }
}
