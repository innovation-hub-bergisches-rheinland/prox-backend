package de.innovationhub.prox.modules.profile.domain.lecturer;

import de.innovationhub.prox.Default;
import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerCreated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerTagged;
import de.innovationhub.prox.modules.profile.domain.user.User;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
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
@Entity
public class Lecturer extends AbstractAggregateRoot {

  @Id
  private UUID id;
  @OneToOne
  private User user;
  private String name;

  @OneToOne
  private LecturerProfile profile;

  private UUID tagCollection;

  public Lecturer(User user, String name) {
    this(UUID.randomUUID(), user, name, null, null);
  }

  @Default
  public Lecturer(UUID id, User user, String name, LecturerProfile profile, UUID tagCollection) {
    this.id = id;
    this.user = user;
    this.name = name;
    this.profile = profile;
    this.tagCollection = tagCollection;
  }

  public static Lecturer create(User user, String name) {
    var createdLecturer = new Lecturer(user, name);
    createdLecturer.registerEvent(LecturerCreated.from(createdLecturer));
    return createdLecturer;
  }

  public void setTagCollection(UUID tagCollection) {
    this.tagCollection = tagCollection;
    this.registerEvent(new LecturerTagged(this.id, this.tagCollection));
  }

  public void setProfile(LecturerProfile profile) {
    this.profile = profile;
    this.registerEvent(LecturerProfileUpdated.from(this.id, this.profile));
  }
}
