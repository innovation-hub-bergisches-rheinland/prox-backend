package de.innovationhub.prox.modules.project.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(modulePort.getAll()).hasSizeGreaterThanOrEqualTo(ModuleEntities.MODULES.size());
  }

  @Test
  void getByKey() {
    assertThat(modulePort.getByKey("BA")).isPresent();
  }
}
