package de.innovationhub.prox.profile.lecturer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Lecturer {

  private final UUID id;
  private final UUID userId;
  private String name;
  private LecturerProfile profile;
  private Set<UUID> tags = new HashSet<>();

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
  }
}
