package de.innovationhub.prox.modules.user.domain.user;

import de.innovationhub.prox.modules.commons.domain.AuditedAggregateRoot;
import java.util.UUID;
import lombok.Getter;


@Getter
public abstract class ProxUser extends AuditedAggregateRoot {

  private final UUID id;

  private final String name;
  private final String email;

  protected ProxUser(UUID id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }
}
