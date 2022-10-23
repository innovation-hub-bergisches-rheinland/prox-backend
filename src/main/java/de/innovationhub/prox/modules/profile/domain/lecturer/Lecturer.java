package de.innovationhub.prox.modules.profile.domain.lecturer;

import de.innovationhub.prox.Default;
import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerCreated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerTagged;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import java.util.UUID;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
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

  @Embedded
  @NotNull
  private UserAccount user;
  private String name;

  @OneToOne
  private LecturerProfile profile;

  private LecturerTags tags;

  public Lecturer(UserAccount user, String name) {
    this(UUID.randomUUID(), user, name, null);
  }

  @Default
  public Lecturer(UUID id, UserAccount user, String name, LecturerProfile profile) {
    this.id = id;
    this.user = user;
    this.name = name;
    this.profile = profile;
  }

  public static Lecturer create(UserAccount user, String name) {
    var createdLecturer = new Lecturer(user, name);
    createdLecturer.registerEvent(LecturerCreated.from(createdLecturer));
    return createdLecturer;
  }

  public void setTags(LecturerTags tags) {
    this.tags = tags;
    this.registerEvent(new LecturerTagged(this.id, this.tags.getTagCollectionId()));
  }

  public void setProfile(LecturerProfile profile) {
    this.profile = profile;
    this.registerEvent(LecturerProfileUpdated.from(this.id, this.profile));
  }
}
