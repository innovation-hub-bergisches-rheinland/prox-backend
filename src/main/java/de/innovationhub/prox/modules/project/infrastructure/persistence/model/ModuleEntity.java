package de.innovationhub.prox.modules.project.infrastructure.persistence.model;

import de.innovationhub.prox.modules.commons.infrastructure.persistence.BaseEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ModuleEntity extends BaseEntity {

  @Id
  private String key;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private boolean active;

  @ElementCollection
  private List<String> disciplines;

  public ModuleEntity(String key, String name, boolean active) {
    this.key = key;
    this.name = name;
    this.active = true;
  }
}
