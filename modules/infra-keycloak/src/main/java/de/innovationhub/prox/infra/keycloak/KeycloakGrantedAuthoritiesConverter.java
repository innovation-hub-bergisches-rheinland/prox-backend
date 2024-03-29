package de.innovationhub.prox.infra.keycloak;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

@Slf4j
public class KeycloakGrantedAuthoritiesConverter
    implements Converter<Jwt, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");

    if(realmAccess == null) {
      log.warn("The provided token does not have a realm_access claim. Is it a valid Keycloak Token?");
      realmAccess = Map.of();
    }

    @SuppressWarnings("unchecked")
    var roles = (List<String>) realmAccess.get("roles");
    Stream<GrantedAuthority> roleAuthorities = Stream.empty();
    if (roles != null) {
      roleAuthorities = roles.stream().map(rn -> new SimpleGrantedAuthority("ROLE_" + rn));
    }

    var features = source.getClaimAsStringList("features");
    Stream<GrantedAuthority> featureAuthorities = Stream.empty();
    if (features != null) {
      featureAuthorities = features.stream().map(fn -> new SimpleGrantedAuthority("FEATURE_" + fn));
    }

    return Stream.concat(roleAuthorities, featureAuthorities).toList();
  }
}
