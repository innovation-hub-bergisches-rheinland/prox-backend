package de.innovationhub.prox.modules.auth.contract;

import java.util.UUID;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
  UUID currentAuthenticatedId();
  Authentication getAuthentication();
}
