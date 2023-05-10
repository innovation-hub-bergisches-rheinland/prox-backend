package de.innovationhub.prox.modules.user.domain.search;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectSearch {
  private Boolean enabled = false;

  @ElementCollection
  private Set<String> moduleTypes = new HashSet<>();

  @ElementCollection
  private Set<String> specializations = new HashSet<>();
}
