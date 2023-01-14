package de.innovationhub.prox.modules.user.application.user;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.exception.UnauthenticatedException;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import de.innovationhub.prox.modules.user.domain.user.ProxUserAccountRepository;
import io.micrometer.core.lang.Nullable;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@ApplicationComponent
@Slf4j
public class SpringSecurityAuthenticationFacadeImpl implements AuthenticationFacade {

  private final ProxUserAccountRepository userRepository;

  @Override
  public UUID currentAuthenticatedId() {
    var auth = getAuthentication();

    if (auth == null) {
      throw new UnauthenticatedException();
    }

    try {
      return UUID.fromString(auth.getName());
    } catch (IllegalArgumentException e) {
      log.error("Invalid UUID in authentication: {}", auth.getName());
      throw new UnauthenticatedException();
    }
  }

  @Override
  public @Nullable Authentication getAuthentication() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      return null;
    }

    return auth;
  }
}
