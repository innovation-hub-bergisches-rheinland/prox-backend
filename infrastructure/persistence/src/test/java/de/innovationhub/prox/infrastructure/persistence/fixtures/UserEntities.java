package de.innovationhub.prox.infrastructure.persistence.fixtures;

import de.innovationhub.prox.infrastructure.persistence.model.UserEntity;
import java.util.List;
import java.util.UUID;

public class UserEntities {

  public static UserEntity HOMER = new UserEntity(UUID.randomUUID(), "Homer Simpson",
    "homer.simpson@example.com");
  public static UserEntity MARGE = new UserEntity(UUID.randomUUID(), "Marge Simpson",
    "marge.simpson@example.com");

  public static List<UserEntity> USERS = List.of(HOMER, MARGE);
}
