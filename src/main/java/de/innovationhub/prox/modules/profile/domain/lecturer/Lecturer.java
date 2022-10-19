package de.innovationhub.prox.modules.profile.domain.lecturer;

import de.innovationhub.prox.Default;
import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerCreated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerTagged;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.ElementCollection;
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
  private UUID userId;
  private String name;

  @OneToOne
  private LecturerProfile profile;

  @ElementCollection
  private Set<UUID> tags = new HashSet<>();

  public static Lecturer create(UUID userId, String name) {
    var createdLecturer = new Lecturer(userId, name);
    createdLecturer.registerEvent(LecturerCreated.from(createdLecturer));
    return createdLecturer;
  }

  public Lecturer(UUID userId, String name) {
    this(UUID.randomUUID(), userId, name, null, new HashSet<>());
  }

  @Default
  public Lecturer(UUID id, UUID userId, String name, LecturerProfile profile, Set<UUID> tags) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.profile = profile;
    this.tags = tags;
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
    this.registerEvent(LecturerTagged.from(this.id, List.copyOf(this.tags)));
  }

  public void setProfile(LecturerProfile profile) {
    this.profile = profile;
    this.registerEvent(LecturerProfileUpdated.from(this.id, this.profile));
  }
}
