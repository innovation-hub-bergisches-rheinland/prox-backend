package de.innovationhub.prox.modules.user.domain.user;

import java.util.UUID;

public class StandardUser extends ProxUser {

  public StandardUser(UUID id, String name, String email) {
    super(id, name, email);
  }
}
