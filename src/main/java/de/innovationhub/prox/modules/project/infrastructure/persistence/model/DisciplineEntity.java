package de.innovationhub.prox.modules.project.infrastructure.persistence.model;

import de.innovationhub.prox.modules.commons.infrastructure.persistence.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DisciplineEntity extends BaseEntity {

  @Id
  private String key;

  @Column(nullable = false)
  private String name;

  public DisciplineEntity(String key, String name) {
    this.key = key;
    this.name = name;
  }
}