package de.innovationhub.prox.infra.keycloak;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakCacheConfig {
  public static final String KEYCLOAK_USER_CACHE_NAME = "keycloak-users";
  public static final String KEYCLOAK_USERS_SEARCH_CACHE_NAME = "keycloak-user-search";
  public static final String KEYCLOAK_USERS_IN_ROLE_CACHE_NAME = "keycloak-users-in-role";
}
