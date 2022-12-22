package de.innovationhub.prox.modules.project.domain.discipline;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Discipline extends AbstractAggregateRoot {

  @Id
  private String key;
  private String name;

  public Discipline(String key, String name) {
    this.key = key;
    this.name = name;
  }
}
