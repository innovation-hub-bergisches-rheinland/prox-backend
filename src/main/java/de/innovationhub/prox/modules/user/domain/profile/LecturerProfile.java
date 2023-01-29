package de.innovationhub.prox.modules.user.domain.profile;

import de.innovationhub.prox.commons.Default;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Lecturers are authorized to teach courses. In PROX we don't really care about a differentiation
 * between professors and research associates.
 */
@Getter
@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Entity
public class LecturerProfile {

  @Id
  private UUID id;

  private Boolean visibleInPublicSearch;

  // TODO: Might be a good idea add attributes directly here?
  @Embedded
  private LecturerProfileInformation profile;

  @Default
  public LecturerProfile(UUID id, Boolean visibleInPublicSearch,
      LecturerProfileInformation profile) {
    this.id = id;
    this.visibleInPublicSearch = visibleInPublicSearch;
    this.profile = profile;
  }

  void setVisibleInPublicSearch(Boolean visibleInPublicSearch) {
    this.visibleInPublicSearch = visibleInPublicSearch;
  }

  void setProfile(LecturerProfileInformation profile) {
    this.profile = profile;
  }
}
