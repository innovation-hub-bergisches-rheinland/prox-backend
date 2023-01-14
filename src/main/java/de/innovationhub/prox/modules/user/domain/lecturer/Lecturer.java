package de.innovationhub.prox.modules.user.domain.lecturer;

import de.innovationhub.prox.Default;
import de.innovationhub.prox.modules.user.domain.ProxUser;
import de.innovationhub.prox.modules.user.domain.lecturer.events.LecturerCreated;
import de.innovationhub.prox.modules.user.domain.lecturer.events.LecturerProfileUpdated;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
public class Lecturer extends ProxUser {

  // TODO: Might be a good idea add attributes directly here?
  @Embedded
  private LecturerProfileInformation profile;

  public Lecturer(UUID userId, String name) {
    super(userId, false, userId, name);
  }

  @Default
  public Lecturer(UUID id, Boolean visibleInPublicSearch, UUID userId, String name,
      LecturerProfileInformation profile) {
    super(id, visibleInPublicSearch, userId, name);
    this.profile = profile;
  }

  public static Lecturer create(UUID user, String name) {
    var createdLecturer = new Lecturer(user, name);
    createdLecturer.registerEvent(LecturerCreated.from(createdLecturer));
    return createdLecturer;
  }


  public void setProfile(LecturerProfileInformation profile) {
    this.profile = profile;
    this.registerEvent(LecturerProfileUpdated.from(this.getId(), this.profile));
  }
}
