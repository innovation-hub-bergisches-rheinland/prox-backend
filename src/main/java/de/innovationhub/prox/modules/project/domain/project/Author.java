package de.innovationhub.prox.modules.project.domain.project;


import de.innovationhub.prox.modules.commons.domain.ReferenceObject;
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
public class Author implements ReferenceObject {
  @NotNull
  private UUID userId;

  public Author(UUID userId) {
    Objects.requireNonNull(userId);

    this.userId = userId;
  }
}
