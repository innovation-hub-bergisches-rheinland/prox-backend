package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A CurriculumContext is the context in which a project should be carried out. For example, a
 * project can be offered as a interdisciplinary project so that both engineering and computer
 * science students can participate. Or it is suited for both a master thesis or a bachelor thesis.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CurriculumContext {

  public static final CurriculumContext EMPTY =
      new CurriculumContext(new ArrayList<>(), new ArrayList<>());

  @Id
  private UUID id = UUID.randomUUID();

  @ManyToMany
  private List<Discipline> disciplines;

  @ManyToMany
  private List<ModuleType> moduleTypes;

  public CurriculumContext(List<Discipline> disciplines, List<ModuleType> moduleTypes) {
    this.disciplines = disciplines;
    this.moduleTypes = moduleTypes;
  }

  public void addDisciplines(Discipline... disciplines) {
    this.disciplines.addAll(List.of(disciplines));
  }

  public void removeDisciplines(Discipline... disciplines) {
    this.disciplines.removeAll(List.of(disciplines));
  }

  public void addModules(ModuleType... modules) {
    this.moduleTypes.addAll(List.of(modules));
  }

  public void removeModules(ModuleType... modules) {
    this.moduleTypes.removeAll(List.of(modules));
  }
}
