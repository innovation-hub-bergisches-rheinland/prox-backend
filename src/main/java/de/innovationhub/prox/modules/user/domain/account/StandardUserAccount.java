package de.innovationhub.prox.modules.user.domain.account;

import java.util.UUID;

public class StandardUserAccount extends ProxUserAccount {

  public StandardUserAccount(UUID id, String name, String email) {
    super(id, name, email);
  }
}
