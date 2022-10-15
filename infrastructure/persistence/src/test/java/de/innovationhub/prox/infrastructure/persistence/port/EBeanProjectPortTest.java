package de.innovationhub.prox.infrastructure.persistence.port;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.infrastructure.persistence.ProjectEntities;
import io.ebean.DB;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EBeanProjectPortTest {

  EBeanProjectPort projectPort = new EBeanProjectPort();

  @BeforeAll
  static void setup() {
    DB.saveAll(ProjectEntities.PROJECTS);
  }

  @Test
  void getById() {
    assertThat(projectPort.getById(ProjectEntities.DRINKING_DUFF.getId()))
      .isPresent();
  }

  @Test
  void getAll() {
    assertThat(projectPort.getAll())
      .hasSizeGreaterThanOrEqualTo(ProjectEntities.PROJECTS.size());
  }
}
