package de.innovationhub.prox.modules.project.domain.project;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ProjectStatus {

  @Enumerated(EnumType.STRING)
  private ProjectState state;
  private Instant updatedAt;

  void updateState(ProjectState state) {
    Objects.requireNonNull(state);

    this.state = state;
    this.updatedAt = Instant.now();
  }
}
