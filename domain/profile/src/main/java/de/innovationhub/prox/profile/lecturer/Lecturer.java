package de.innovationhub.prox.profile.lecturer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
public class Lecturer {

  private final UUID id;
  private final UUID user;
  private String name;
  private LecturerProfile profile;
  @Builder.Default
  private Set<UUID> tags = new HashSet<>();

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
  }

  public Lecturer(UUID user) {
    this.user = user;
    this.id = user;
  }
}
