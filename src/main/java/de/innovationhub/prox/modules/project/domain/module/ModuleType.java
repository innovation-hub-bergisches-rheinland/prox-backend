package de.innovationhub.prox.modules.project.domain.module;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModuleType extends AbstractAggregateRoot {

  @Id
  private String key;
  private String name;

  @ElementCollection
  private List<String> disciplines;

  private boolean active = true;

  public ModuleType(String key, String name, List<String> disciplines) {
    this.key = key;
    this.name = name;
    this.disciplines = disciplines;
  }
}
