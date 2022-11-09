package de.innovationhub.prox.modules.auth.application;

import static org.junit.jupiter.api.Assertions.*;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.commons.application.exception.UnauthenticatedException;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;

class SpringSecurityAuthenticationFacadeImplTest extends AbstractIntegrationTest {

  @Autowired
  private SpringSecurityAuthenticationFacadeImpl authenticationFacade;

  @Test
  void shouldThrowWhenUnauthenticated() {
    assertThrows(UnauthenticatedException.class, () -> authenticationFacade.currentAuthenticated());
  }

  @Test
  @WithMockUser(username = "5f230652-c1d7-40f5-b076-f0a9baa684dd")
  void shouldReturnMockUser() {
    assertEquals("5f230652-c1d7-40f5-b076-f0a9baa684dd", authenticationFacade.currentAuthenticated().toString());
  }

  @Test
  void shouldReturnSubClaimOfJwtAuth() {
    Jwt jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", "0d77461b-748e-4f1d-891a-2749f9746018")
        .build();
    Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("SCOPE_read");
    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities);
    var context = SecurityContextHolder.getContext();
    context.setAuthentication(token);

    assertEquals("0d77461b-748e-4f1d-891a-2749f9746018", authenticationFacade.currentAuthenticated().toString());
  }
}