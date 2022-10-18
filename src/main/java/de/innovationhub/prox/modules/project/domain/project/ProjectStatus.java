package de.innovationhub.prox.modules.project.domain.project;

import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class ProjectStatus {

  private ProjectState state;
  private Instant updatedAt;

  void updateState(ProjectState state) {
    Objects.requireNonNull(state);

    this.state = state;
    this.updatedAt = Instant.now();
  }
}
