package de.innovationhub.prox.utils;

import de.innovationhub.prox.commons.exception.UnauthenticatedException;
import java.util.UUID;
import org.springframework.security.core.Authentication;

public class SecurityUtils {

  private SecurityUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static UUID extractUserId(Authentication authentication) {
    if (!authentication.isAuthenticated()) {
      throw new UnauthenticatedException();
    }
    return UUID.fromString(authentication.getName());
  }
}
