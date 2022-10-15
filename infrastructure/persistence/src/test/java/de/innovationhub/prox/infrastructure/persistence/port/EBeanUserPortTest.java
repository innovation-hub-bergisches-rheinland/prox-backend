package de.innovationhub.prox.infrastructure.persistence.port;

import static de.innovationhub.prox.infrastructure.persistence.UserEntities.HOMER;
import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.infrastructure.persistence.UserEntities;
import de.innovationhub.prox.infrastructure.persistence.model.query.QUserEntity;
import de.innovationhub.prox.profile.user.User;
import io.ebean.DB;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EBeanUserPortTest {

  EBeanUserPort userPort = new EBeanUserPort();

  @BeforeAll
  static void setup() {
    DB.saveAll(UserEntities.USERS);
  }

  @Test
  void save() {
    var maggie = new User(UUID.randomUUID(), "Maggie Simpson",
      "maggie.simpson@example.com");

    var saved = userPort.save(maggie);

    assertThat(saved)
      .isNotNull();

    var found = new QUserEntity()
      .id.eq(maggie.getId())
      .findOneOrEmpty();

    assertThat(found)
      .isPresent();
  }

  @Test
  void getById() {
    assertThat(userPort.getById(HOMER.getId()))
      .isPresent();
  }
}
