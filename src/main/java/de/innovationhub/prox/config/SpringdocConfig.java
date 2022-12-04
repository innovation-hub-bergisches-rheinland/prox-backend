package de.innovationhub.prox.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfig {

  private final BuildProperties buildProperties;

  private final String resourceServerUrl;

  public SpringdocConfig(BuildProperties buildProperties,
      @Value("spring.security.oauth2.resourceserver.jwt.issuer-uri") String resourceServerUrl
  ) {
    this.buildProperties = buildProperties;
    this.resourceServerUrl = resourceServerUrl;
  }

  @Bean
  public OpenAPI openAPIDefinition() {
    return new OpenAPI()
        .info(new Info()
            .title("Prox API")
            .description("Prox API Documentation")
            .version(buildProperties.getVersion()))
        .externalDocs(new ExternalDocumentation()
            .description("PROX Backend GitHub Repository")
            .url("https://github.com/innovation-hub-bergisches-rheinland/prox-backend")
        )
        .components(
            new Components()
                .addSecuritySchemes("oidc", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"))
                .addSecuritySchemes("oidc",
                    new SecurityScheme()
                        .type(Type.OPENIDCONNECT)
                        .openIdConnectUrl(
                            resourceServerUrl + "/.well-known/openid-configuration")
                )
        );
  }
}
