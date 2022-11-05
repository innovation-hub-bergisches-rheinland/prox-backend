package de.innovationhub.prox.modules.auth.application;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.exception.UnauthenticatedException;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityAuthenticationFacadeImpl implements AuthenticationFacade {

  @Override
  public UUID currentAuthenticated() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if(!auth.isAuthenticated()) throw new UnauthenticatedException();

    // Name should always be a UUID. Investigate a better way.
    return UUID.fromString(auth.getName());
  }
}
