package de.innovationhub.prox.modules.project.domain.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class ProjectStatusTest {
  @Test
  void shouldUpdateTimestamp() {
    var currentMoment = Instant.now();
    var status = new ProjectStatus(ProjectState.ARCHIVED, currentMoment);

    status.updateState(ProjectState.RUNNING);

    assertThat(status.getUpdatedAt()).isNotSameAs(currentMoment).isAfterOrEqualTo(currentMoment);
  }
}
