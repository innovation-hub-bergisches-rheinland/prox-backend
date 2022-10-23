package de.innovationhub.prox.modules.profile.domain.user;

import de.innovationhub.prox.modules.commons.domain.ReferenceObject;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Users most likely will be created by our Identity Provider (Keycloak).
// We still need a representation to it in our domain model.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Getter
@EqualsAndHashCode
public class UserAccount implements ReferenceObject {

  private UUID userId;

  public UserAccount(UUID userId) {
    Objects.requireNonNull(userId);

    this.userId = userId;
  }
}
