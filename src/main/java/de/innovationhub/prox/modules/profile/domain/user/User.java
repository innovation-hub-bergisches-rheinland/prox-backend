package de.innovationhub.prox.modules.profile.domain.user;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Users most likely will be created by our Identity Provider (Keycloak).
// We still need a representation to it in our domain model.
@EqualsAndHashCode(callSuper = false)
@Data
public class User {

  private final UUID id;

  @NotBlank
  @Size(max = 1024)
  private String name;

  @Email
  private String email;

  public User(UUID id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }
}
