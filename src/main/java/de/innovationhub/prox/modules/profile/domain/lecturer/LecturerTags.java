package de.innovationhub.prox.modules.profile.domain.lecturer;

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
public class LecturerTags implements ReferenceObject {
  @NotNull
  private UUID tagCollectionId;

  public LecturerTags(UUID tagCollectionId) {
    Objects.requireNonNull(tagCollectionId);

    this.tagCollectionId = tagCollectionId;
  }
}
