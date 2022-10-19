package de.innovationhub.prox.modules.profile.domain.user;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.profile.domain.user.events.UserRegistered;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Users most likely will be created by our Identity Provider (Keycloak).
// We still need a representation to it in our domain model.
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends AbstractAggregateRoot {

  @Id
  private UUID id;

  @NotBlank
  @Size(max = 1024)
  private String name;

  @Email
  private String email;

  public static User register(UUID id, String name, String email) {
    var createdUser = new User(id, name, email);
    createdUser.registerEvent(UserRegistered.from(createdUser));
    return createdUser;
  }

  public User(UUID id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }
}
