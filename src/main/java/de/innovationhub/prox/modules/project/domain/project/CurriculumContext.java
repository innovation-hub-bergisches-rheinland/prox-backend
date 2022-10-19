package de.innovationhub.prox.modules.project.domain.project;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A CurriculumContext is the context in which a project should be carried out. For example, a
 * project can be offered as a interdisciplinary project so that both engineering and computer
 * science students can participate. Or it is suited for both a master thesis or a bachelor thesis.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurriculumContext {

  public static final CurriculumContext EMPTY =
      new CurriculumContext(new HashSet<>(), new HashSet<>());

  @Id
  private UUID id = UUID.randomUUID();

  @ElementCollection
  private Set<UUID> disciplines;
  @ElementCollection
  private Set<UUID> modules;

  public CurriculumContext(Set<UUID> disciplines, Set<UUID> modules) {
    this.disciplines = disciplines;
    this.modules = modules;
  }

  public void addDisciplines(UUID... disciplines) {
    this.disciplines.addAll(Set.of(disciplines));
  }

  public void removeDisciplines(UUID... disciplines) {
    this.disciplines.removeAll(Set.of(disciplines));
  }

  public void addModules(UUID... modules) {
    this.modules.addAll(Set.of(modules));
  }

  public void removeModules(UUID... modules) {
    this.modules.removeAll(Set.of(modules));
  }
}
