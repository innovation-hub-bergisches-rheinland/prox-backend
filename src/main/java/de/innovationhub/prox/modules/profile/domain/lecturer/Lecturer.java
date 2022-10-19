package de.innovationhub.prox.modules.profile.domain.lecturer;

import de.innovationhub.prox.Default;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Lecturers are authorized to teach courses. In PROX we don't really care about a differentiation
 * between professors and research associates.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class Lecturer {

  private final UUID id;
  private final UUID userId;
  private String name;
  private LecturerProfile profile;
  private Set<UUID> tags = new HashSet<>();

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
  }
}
