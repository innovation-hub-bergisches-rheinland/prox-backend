package de.innovationhub.prox.infrastructure.persistence.model;

import de.innovationhub.prox.project.project.ProjectState;
import java.time.Instant;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatusEmbeddable {

  @Enumerated(EnumType.STRING)
  private ProjectState state;
  private Instant updatedAt;
}
