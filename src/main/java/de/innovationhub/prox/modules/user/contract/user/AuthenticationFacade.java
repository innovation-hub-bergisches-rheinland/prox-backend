package de.innovationhub.prox.modules.user.contract.user;

import java.util.UUID;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

  UUID currentAuthenticatedId();

  Authentication getAuthentication();

  boolean isAuthenticated();
}
