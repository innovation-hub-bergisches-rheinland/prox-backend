package de.innovationhub.prox.modules.project.domain.module;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
