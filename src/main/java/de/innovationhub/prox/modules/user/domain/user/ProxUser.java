package de.innovationhub.prox.modules.user.domain.user;

import de.innovationhub.prox.modules.commons.domain.AuditedAggregateRoot;
import java.util.UUID;
import lombok.Getter;


@Getter
public class ProxUser extends AuditedAggregateRoot {

  private final UUID id;

  private final String name;
  private final String email;

  public ProxUser(UUID id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }
}
