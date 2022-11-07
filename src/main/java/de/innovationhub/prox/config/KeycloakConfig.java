package de.innovationhub.prox.config;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

  @Value("${keycloak.credentials.secret}")
  private String secret;
  @Value("${keycloak.client-id}")
  private String clientId;
  @Value("${keycloak.auth-server-url}")
  private String authUrl;
  @Value("${keycloak.realm}")
  private String realm;

  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder.builder()
        .grantType(CLIENT_CREDENTIALS)
        .serverUrl(authUrl)
        .realm(realm)
        .clientId(clientId)
        .clientSecret(secret)
        .build();
  }

  @Bean
  public RealmResource realmResource(Keycloak keycloak) {
    return keycloak.realm(realm);
  }
}
