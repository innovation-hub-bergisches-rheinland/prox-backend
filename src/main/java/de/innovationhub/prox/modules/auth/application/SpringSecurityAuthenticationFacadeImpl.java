package de.innovationhub.prox.modules.auth.application;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.exception.UnauthenticatedException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@ApplicationComponent
public class SpringSecurityAuthenticationFacadeImpl implements AuthenticationFacade {

  @Override
  public UUID currentAuthenticatedId() {
    var auth = getAuthentication();

    // Name should always be a UUID. Investigate a better way.
    return UUID.fromString(auth.getName());
  }

  @Override
  public Authentication getAuthentication() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if(auth == null || !auth.isAuthenticated()) throw new UnauthenticatedException();

    return auth;
  }
}
