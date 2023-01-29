package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.commons.buildingblocks.ReferenceObject;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class InterestedUser implements ReferenceObject {
  @NotNull
  private UUID userId;

  public InterestedUser(UUID userId) {
    Objects.requireNonNull(userId);

    this.userId = userId;
  }
}
