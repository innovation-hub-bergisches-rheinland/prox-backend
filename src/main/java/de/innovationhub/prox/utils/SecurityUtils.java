package de.innovationhub.prox.utils;

import de.innovationhub.prox.commons.exception.UnauthenticatedException;
import java.util.UUID;
import org.springframework.security.core.Authentication;

public class SecurityUtils {

  private SecurityUtils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Extracts the User ID from a given Authentication.
   *
   * @param authentication the authentication to extract user ID from
   * @return User ID
   * @throws UnauthenticatedException when a user is not authenticated
   * @throws IllegalArgumentException when the authentication is not a valid ID
   */
  public static UUID extractUserId(Authentication authentication) {
    if (!authentication.isAuthenticated()) {
      throw new UnauthenticatedException();
    }
    return UUID.fromString(authentication.getName());
  }
}
