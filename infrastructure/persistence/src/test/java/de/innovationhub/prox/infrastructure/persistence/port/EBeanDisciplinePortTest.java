package de.innovationhub.prox.infrastructure.persistence.port;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.infrastructure.persistence.DisciplineEntities;
import io.ebean.DB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EBeanDisciplinePortTest {

  EBeanDisciplinePort disciplinePort = new EBeanDisciplinePort();

  @BeforeAll
  static void setup() {
    DB.saveAll(DisciplineEntities.DISCIPLINES);
  }

  @Test
  void getAll() {
    assertThat(disciplinePort.getAll())
      .hasSizeGreaterThanOrEqualTo(DisciplineEntities.DISCIPLINES.size());
  }

  @Test
  void getByKey() {
    assertThat(disciplinePort.getByKey("INF"))
      .isPresent();
  }
}
