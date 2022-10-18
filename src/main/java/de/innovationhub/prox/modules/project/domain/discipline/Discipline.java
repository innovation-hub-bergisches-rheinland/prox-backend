package de.innovationhub.prox.modules.project.domain.discipline;

import lombok.Data;

@Data
public class Discipline {
  private final String key;
  private final String name;

  public Discipline(String key, String name) {
    this.key = key;
    this.name = name;
  }
}
