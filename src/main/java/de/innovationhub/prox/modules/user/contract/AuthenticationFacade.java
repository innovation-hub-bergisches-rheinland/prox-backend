package de.innovationhub.prox.modules.user.contract;

import de.innovationhub.prox.modules.user.domain.ProxUser;
import java.util.UUID;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
  UUID currentAuthenticatedId();

  Authentication getAuthentication();

  ProxUser getAuthenticatedUser();
}
