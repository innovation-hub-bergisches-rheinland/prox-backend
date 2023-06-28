package de.innovationhub.prox.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityConfig {

  private final JwtAuthenticationConverter jwtAuthenticationConverter;

  @Bean
  @Profile("unsecure")
  public SecurityFilterChain unsecureSecurityFilterChain(HttpSecurity http) throws Exception {
    http.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(registry -> registry.anyRequest().permitAll());

    return http.build();
  }

  @Bean
  @Primary
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.cors(cors -> cors.configurationSource(request -> {
          var corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
          corsConfiguration.addAllowedMethod("*");
          return corsConfiguration;
        }))
        .csrf(AbstractHttpConfigurer::disable)
        .anonymous(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
        .authorizeHttpRequests(
            registry ->
                registry
                    .requestMatchers(HttpMethod.GET,
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/v3/api-docs/swagger-config"
                    )
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/projects/**", "/tags/**",
                        "/organizations/**",
                        "/lecturers/**", "/disciplines/**", "/moduleTypes/**", "/users/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/users/search")
                    .authenticated()
                    .requestMatchers("/user/profile/lecturer/**")
                    .hasRole("professor")
                    .requestMatchers("/user/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/actuator/health/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/tags/*/merge")
                    .hasRole("admin")
                    .requestMatchers(HttpMethod.POST, "/tags/*/split")
                    .hasRole("admin")
                    .requestMatchers(HttpMethod.PUT, "/tags/*/aliases")
                    .hasRole("admin")
                    .requestMatchers(HttpMethod.DELETE, "/tags/**")
                    .hasRole("admin")
                    .requestMatchers("/projects/**", "/tags/**", "/organizations/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .anyRequest()
                    .denyAll()
        );

    return http.build();
  }
}