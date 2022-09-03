package de.innovationhub.prox.project.project;

import java.time.Instant;
import java.util.Objects;

public class ProjectStatus {
  private ProjectState state;
  private Instant updatedAt;

  public void updateState(ProjectState state) {
    Objects.requireNonNull(state);

    this.state = state;
    this.updatedAt = Instant.now();
  }
}
