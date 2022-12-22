package de.innovationhub.prox.modules.profile.domain.lecturer;

import de.innovationhub.prox.Default;
import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerAvatarSet;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerCreated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerRenamed;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerTagged;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerVisibilityChanged;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
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
public class Lecturer extends AbstractAggregateRoot {

  @Id
  private UUID id;

  private Boolean visibleInPublicSearch = false;

  @NotNull
  private UUID userId;
  private String name;

  @Embedded
  private LecturerProfile profile;

  @ElementCollection
  private Set<UUID> tags = new HashSet<>();

  private String avatarKey;

  public Lecturer(UUID userId, String name) {
    this(UUID.randomUUID(), userId, name, null);
  }

  @Default
  public Lecturer(UUID id, UUID userId, String name, LecturerProfile profile) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.profile = profile;
  }

  public static Lecturer create(UUID user, String name) {
    var createdLecturer = new Lecturer(user, name);
    createdLecturer.registerEvent(LecturerCreated.from(createdLecturer));
    return createdLecturer;
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
    this.registerEvent(new LecturerTagged(this.id, this.tags));
  }

  public void setProfile(LecturerProfile profile) {
    this.profile = profile;
    this.registerEvent(LecturerProfileUpdated.from(this.id, this.profile));
  }

  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Lecturer title cannot be null");
    }
    if (this.name.equals(name)) {
      return;
    }

    this.name = name;
    this.registerEvent(new LecturerRenamed(this.id, this.name));
  }

  public void setAvatarKey(String avatarKey) {
    this.avatarKey = avatarKey;
    this.registerEvent(new LecturerAvatarSet(this.id, this.avatarKey));
  }

  public void setVisibleInPublicSearch(boolean visible) {
    this.visibleInPublicSearch = visible;
    this.registerEvent(new LecturerVisibilityChanged(this.id, this.visibleInPublicSearch));
  }
}
