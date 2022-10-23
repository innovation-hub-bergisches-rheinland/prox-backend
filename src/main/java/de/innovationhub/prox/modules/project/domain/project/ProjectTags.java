package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.modules.commons.domain.ReferenceObject;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectTags implements ReferenceObject {
  @NotNull
  private UUID tagCollectionId;

  public ProjectTags(UUID tagCollectionId) {
    Objects.requireNonNull(tagCollectionId);

    this.tagCollectionId = tagCollectionId;
  }
}
