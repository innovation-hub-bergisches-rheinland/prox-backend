package de.innovationhub.prox.modules.auth.contract;

import java.util.UUID;

public interface AuthenticationFacade {
  UUID currentAuthenticated();
}
