package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.commons.buildingblocks.ReferenceObject;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Supervisor implements ReferenceObject {

  @NotNull
  private UUID lecturerId;

  public Supervisor(UUID lecturerId) {
    Objects.requireNonNull(lecturerId);

    this.lecturerId = lecturerId;
  }
}
