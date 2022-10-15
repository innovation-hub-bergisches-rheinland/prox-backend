package de.innovationhub.prox.infrastructure.persistence.port;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.infrastructure.persistence.ModuleEntities;
import io.ebean.DB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EBeanModulePortTest {

  EBeanModulePort modulePort = new EBeanModulePort();

  @BeforeAll
  static void setup() {
    DB.saveAll(ModuleEntities.MODULES);
  }

  @Test
  void getAll() {
    assertThat(modulePort.getAll())
      .hasSizeGreaterThanOrEqualTo(ModuleEntities.MODULES.size());
  }

  @Test
  void getByKey() {
    assertThat(modulePort.getByKey("BA"))
      .isPresent();
  }
}
