package de.innovationhub.prox.modules.project.domain.module;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ModuleType extends AbstractAggregateRoot {

  @Id
  private String key;
  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Discipline> disciplines;

  private boolean active = true;

  public ModuleType(String key, String name, List<Discipline> disciplines) {
    this.key = key;
    this.name = name;
    this.disciplines = disciplines;
  }
}
