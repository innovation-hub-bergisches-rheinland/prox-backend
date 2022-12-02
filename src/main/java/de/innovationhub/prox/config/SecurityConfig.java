package de.innovationhub.prox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
class SecurityConfig {

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakGrantedAuthoritiesConverter());
    jwtConverter.setPrincipalClaimName("sub");
    return jwtConverter;
  }

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .anonymous()
        .disable()
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()))
        .authorizeHttpRequests(
            registry ->
                registry
                    .mvcMatchers(HttpMethod.GET, "/projects/**", "/tags/**", "/organizations/**",
                        "/lecturers/**")
                    .permitAll()
                    .mvcMatchers(HttpMethod.GET, "/users/search")
                    .authenticated()
                    .mvcMatchers(HttpMethod.GET, "/actuator/health/**")
                    .permitAll()
                    .mvcMatchers("/projects/**", "/tags/**", "/organizations/**", "/lecturers/**")
                    .authenticated()
                    .anyRequest()
                    .denyAll()
        );

    return http.build();
  }
}